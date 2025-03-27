package sn.groupeisi.flexipay.interfaces;

public interface IOtpAuthentification {
    public void genererEtEnvoyerOtp(Long transactionId, String canal);
}
