package sn.groupeisi.flexipay.interfaces;

import sn.groupeisi.flexipay.entities.Transaction;
import sn.groupeisi.flexipay.enums.TypeTransaction;

import java.util.List;

public interface ITransaction {
    public void effectuerTransactionSansOtp(String numeroCarte, double montant, String commercant,
                                            TypeTransaction typeTransaction, String codePin);
    public void annulerTransaction(Long transactionId);
    public void validerTransactionAvecOtp(Long transactionId, String otpSaisi);
    public List<Transaction> getAllTransactions();
}
