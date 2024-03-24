package infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.model.*;
import domain.port.inbound.*;
import domain.port.outbound.WithdrawalMoneyOutbound;
import domain.port.outbound.model.EventType;
import domain.usecase.*;
import domain.usecase.util.DepositAccountRetry;
import domain.usecase.util.WithdrawAccountRetry;
import domain.usecase.util.WithdrawalStatusAccountRetry;
import infrastructure.adapter.driven.adapter.InMemoryAccountAdapter;
import infrastructure.adapter.driven.adapter.InMemoryOperationAdapter;
import infrastructure.adapter.driven.adapter.WithdrawalMoneyAdapter;
import infrastructure.adapter.driven.handler.EventHandler;
import infrastructure.adapter.driven.publisher.EventPublisher;
import infrastructure.adapter.driving.adapter.AccountFetchAdapter;
import infrastructure.adapter.driving.adapter.AccountOperationStatusAdapter;
import infrastructure.adapter.driving.adapter.AccountTransferAdapter;
import infrastructure.adapter.driving.adapter.AccountWithdrawAdapter;
import infrastructure.adapter.driving.listener.EventListener;
import infrastructure.adapter.driving.processor.*;
import infrastructure.adapter.query.InMemoryAccountQueryAdapter;
import infrastructure.adapter.query.InMemoryOperationQueryAdapter;
import infrastructure.mapper.AccountTransferMapper;
import infrastructure.mapper.AccountTransferResponseMapper;
import infrastructure.mapper.AccountWithdrawMapper;
import infrastructure.mapper.AccountWithdrawResponseMapper;
import infrastructure.thirdparty.WithdrawalService;
import infrastructure.thirdparty.WithdrawalServiceStub;
import infrastructure.util.AccountWithdrawRetryWrapper;
import infrastructure.util.HerokuUtil;
import infrastructure.util.AccountDepositRetryWrapper;
import infrastructure.util.WithdrawalStatusAccountRetryWrapper;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.http.staticfiles.Location;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;


public class Main {

    public static InMemoryOperationAdapter inMemoryOperationAdapter;
    public static InMemoryAccountAdapter inMemoryAccountAdapter;
    public static InMemoryAccountQueryAdapter inMemoryAccountQueryAdapter;
    public static ObjectMapper objectMapper;
    public static AccountTransferMapper accountTransferMapper;
    public static TransferMoneyInbound transferMoneyInbound;
    public static AccountTransferAdapter accountTransferAdapter;
    public static AccountOperationStatusAdapter accountOperationStatusAdapter;
    public static WithdrawalService withdrawalService;
    public static WithdrawalMoneyOutbound withdrawalMoneyOutbound;
    public static WithdrawalMoneyInbound withdrawalMoneyInbound;
    public static AccountWithdrawMapper accountWithdrawMapper;
    public static AccountWithdrawAdapter accountWithdrawAdapter;
    public static AccountFetchAdapter accountFetchAdapter;
    public static ReadWriteLock lock;
    public static EventHandler eventHandler;
    public static EventPublisher eventPublisher;
    public static EventProcessor reserveProcessor;
    public static EventProcessor depositProcessor;
    public static EventProcessor withdrawProcessor;
    public static EventListener eventListener;
    public static AccountWithdrawResponseMapper accountWithdrawResponseMapper;
    public static AccountTransferResponseMapper accountTransferResponseMapper;
    public static InMemoryOperationQueryAdapter inMemoryOperationQueryAdapter;
    public static ReserveAccountInbound reserveAccountInbound;
    public static WithdrawAccountRetry withdrawAccountRetry;
    public static DepositAccountRetry depositAccountRetry;
    public static DepositAccountInbound depositAccountUseCase;
    public static WithdrawalStatusAccountRetry withdrawalStatusAccountRetry;
    public static Map<EventType, EventProcessor> eventProcessors = new HashMap<>();
    public static ConcurrentMap<AccountOperationId, AccountOperation> operations = new ConcurrentHashMap<>();
    public static ConcurrentMap<AccountId, Account> accounts = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        operations = new ConcurrentHashMap<>();
        inMemoryOperationAdapter = new InMemoryOperationAdapter(operations);

        accounts = new ConcurrentHashMap<>();
        AccountId accountId = new AccountId("A534736J685JC79Q7G4");
        Amount amount = new Amount(BigDecimal.valueOf(500));
        accounts.put(new AccountId("A534736J685JC79Q7G4"), new Account(accountId, amount));
        AccountId account1Id = new AccountId("BW481XDNC43CWQXV4XL");
        Amount amount1 = new Amount(BigDecimal.valueOf(400));
        accounts.put(new AccountId("BW481XDNC43CWQXV4XL"), new Account(account1Id, amount1));
        inMemoryAccountAdapter = new InMemoryAccountAdapter(accounts);
        inMemoryAccountQueryAdapter = new InMemoryAccountQueryAdapter(accounts);

        lock = new ReentrantReadWriteLock();
        reserveAccountInbound = new ReserveAccountUseCase(
                inMemoryAccountAdapter,
                inMemoryOperationAdapter,
                lock);

        objectMapper = new ObjectMapper();

        //AccountTransferAdapter
        accountTransferMapper = new AccountTransferMapper(objectMapper);

        eventHandler = new EventHandler();
        eventPublisher = new EventPublisher(eventHandler);

        reserveProcessor = new ReserveEventProcessor(
                inMemoryAccountQueryAdapter,
                reserveAccountInbound,
                inMemoryOperationAdapter);

        inMemoryOperationQueryAdapter = new InMemoryOperationQueryAdapter(operations);
        depositAccountRetry = new AccountDepositRetryWrapper();
        depositAccountUseCase = new DepositAccountUseCase(
                inMemoryAccountAdapter,
                inMemoryOperationAdapter,
                lock,
                inMemoryOperationQueryAdapter,
                depositAccountRetry,
                inMemoryAccountQueryAdapter
        );
        depositProcessor = new DepositEventProcessor(
                inMemoryAccountQueryAdapter,
                inMemoryOperationAdapter,
                depositAccountUseCase);

        withdrawalService = new WithdrawalServiceStub();
        withdrawalMoneyOutbound = new WithdrawalMoneyAdapter(withdrawalService);

        withdrawAccountRetry = new AccountWithdrawRetryWrapper();
        withdrawalStatusAccountRetry = new WithdrawalStatusAccountRetryWrapper();
        withdrawalMoneyInbound = new WithdrawalMoneyUserCase(
                inMemoryOperationAdapter,
                eventPublisher,
                withdrawAccountRetry,
                inMemoryOperationAdapter,
                inMemoryOperationQueryAdapter,
                withdrawalMoneyOutbound,
                inMemoryAccountQueryAdapter,
                inMemoryAccountAdapter,
                withdrawalStatusAccountRetry);
        withdrawProcessor = new WithdrawEventProcessor(withdrawalMoneyInbound);

        eventProcessors.put(EventType.DEPOSIT, depositProcessor);
        eventProcessors.put(EventType.RESERVE, reserveProcessor);
        eventProcessors.put(EventType.WITHDRAW, withdrawProcessor);
        eventListener = new EventListener(eventProcessors, eventHandler);
        Thread thread = new Thread(eventListener::fetch);
        thread.start();

        //AccountTransferAdapter
        transferMoneyInbound = new TransferMoneyUseCase(inMemoryOperationAdapter, eventPublisher);
        accountTransferMapper = new AccountTransferMapper(objectMapper);
        accountTransferResponseMapper = new AccountTransferResponseMapper();
        accountTransferAdapter = new AccountTransferAdapter(transferMoneyInbound, accountTransferMapper, accountTransferResponseMapper);

        //AccountWithdrawAdapter
        accountWithdrawMapper = new AccountWithdrawMapper(objectMapper);
        accountWithdrawResponseMapper = new AccountWithdrawResponseMapper();
        accountWithdrawAdapter = new AccountWithdrawAdapter(withdrawalMoneyInbound, accountWithdrawMapper, accountWithdrawResponseMapper);

        //AccountFetchAdapter
        accountFetchAdapter = new AccountFetchAdapter(inMemoryAccountQueryAdapter);

        //AccountOperationStatusAdapter
        accountOperationStatusAdapter = new AccountOperationStatusAdapter(inMemoryOperationQueryAdapter);

        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public", Location.CLASSPATH);
            config.registerPlugin(new RouteOverviewPlugin("/routes"));
        }).start(HerokuUtil.getHerokuAssignedPort());

        app.routes(() -> {
            post(Path.Web.ACCOUNT_TRANSFER, accountTransferAdapter::transferMoney);
            post(Path.Web.ACCOUNT_WITHDRAW, accountWithdrawAdapter::withdrawMoney);
            get(Path.Web.ACCOUNT_OPERATION, accountOperationStatusAdapter::fetchStatus);
            get(Path.Web.ACCOUNT, accountFetchAdapter::fetchAccount);
        });

    }

}
