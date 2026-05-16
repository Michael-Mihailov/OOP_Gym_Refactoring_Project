public class EnrollMemberResult
{
    public enum Status
    {
        SUCCESS,
        CLASS_FULL,
        ALREADY_ENROLLED,
        INNACTIVE_MEMBER,
        MEMBER_NOT_FOUND,
        CLASS_NOT_FOUND;
    }
    
    private final Status status;
    private final PaymentResult paymentResult;
    
    public EnrollMemberResult(Status status, PaymentResult paymentResult)
    {
        this.status = status;
        this.paymentResult = paymentResult;
    }
    public EnrollMemberResult(Status status)
    {
        this.status = status;
        this.paymentResult = null;
    }
    
    public Status getStatus()
    {
        return status;
    }
    public PaymentResult getPaymentResult()
    {
        return paymentResult;
    }
}