package infrastructure.util;


import io.javalin.http.Context;

public class RequestUtil {

    public static String getBody(Context ctx) {

        return ctx.body();
    }

    public static String getAmount(Context ctx) {
        return ctx.pathParam("amount");
    }

    public static String getOperationNumber(Context ctx) {
        return ctx.pathParam("operationNumber");
    }

    public static String getAccountNumber(Context ctx) {
        return ctx.pathParam("accountNumber");
    }

}
