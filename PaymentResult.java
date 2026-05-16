public class PaymentResult 
{
    public enum Status {
        SUCCESS,
        NOT_FOUND,
        INSUFFICIENT_FUNDS
    }

    private final Status status;
    private final double basePaymentAmount; // before any discounts
    private final double paymentAmount;

    public PaymentResult(Status status, double basePaymentAmount, double discountedPaymentAmount) 
    {
        this.status = status;
        this.basePaymentAmount = basePaymentAmount;
        this.paymentAmount = discountedPaymentAmount;
    }
    public PaymentResult(Status status, double amount) 
    {
        this.status = status;
        this.basePaymentAmount = amount;
        this.paymentAmount = amount;
    }
    public PaymentResult(Status status)
    {
        this.status = status;
        this.basePaymentAmount = 0;
        this.paymentAmount = 0;
    }

    public Status getStatus() 
    {
        return status;
    }

    public double getBasePaymentAmount() 
    {
        return basePaymentAmount;
    }
    public double getPaymentAmount() 
    {
        return paymentAmount;
    }
}