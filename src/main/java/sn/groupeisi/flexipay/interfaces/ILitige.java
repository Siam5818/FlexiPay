package sn.groupeisi.flexipay.interfaces;

public interface ILitige {
    public void signalerLitige(Long transactionId, String motif, Long adminId);
}
