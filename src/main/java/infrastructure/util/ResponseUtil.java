package infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import infrastructure.adapter.driving.dto.AccountResponse;
import infrastructure.adapter.driving.dto.AccountTransferResponse;
import infrastructure.adapter.driving.dto.AccountWithdrawResponse;
import infrastructure.adapter.driving.dto.OperationStatusResponse;
import io.javalin.http.Context;

public class ResponseUtil {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toAccountResponse(AccountResponse response) {
        try {
            return OBJECT_MAPPER.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            //LOG
            return "";
        }
    }

    public static String toAccountTransferResponse(AccountTransferResponse response) {
        try {
            return OBJECT_MAPPER.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            //LOG
            return "";
        }
    }

    public static String toAccountWithdrawResponse(AccountWithdrawResponse response) {
        try {
            return OBJECT_MAPPER.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            //LOG
            return "";
        }
    }

    public static void returnAccountTransferResponse(Context ctx, AccountTransferResponse response) {
        ctx.contentType("application/json");
        ctx.json(toAccountTransferResponse(response));
    }

    public static void returnAccountResponse(Context ctx, AccountResponse response) {
        ctx.contentType("application/json");
        ctx.json(toAccountResponse(response));
    }

    public static void returnAccountWithdrawResponse(Context ctx, AccountWithdrawResponse response) {
        ctx.contentType("application/json");
        ctx.json(toAccountWithdrawResponse(response));
    }

    public static void returnInvalidRequestBodyResponse(Context ctx) {
        ctx.contentType("application/json");
        ctx.status(400).result("Invalid request body");
    }

    public static void returnOperationStatusResponse(Context ctx, OperationStatusResponse response) {
        ctx.contentType("application/json");
        ctx.json(toOperationStatusResponse(response));
    }

    public static void returnEmptyOperationStatusResponse(Context ctx) {
        ctx.contentType("application/json");
        ctx.status(404).result(String.format("No found account operation %s", RequestUtil.getOperationNumber(ctx)));
    }

    private static Object toOperationStatusResponse(OperationStatusResponse response) {
        try {
            return OBJECT_MAPPER.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            //LOG
            return "";
        }
    }

}
