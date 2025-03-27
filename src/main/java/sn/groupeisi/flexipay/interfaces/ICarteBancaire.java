package sn.groupeisi.flexipay.interfaces;

import sn.groupeisi.flexipay.entities.CarteBancaire;
import sn.groupeisi.flexipay.enums.StatutCarte;
import sn.groupeisi.flexipay.enums.TypeCarte;

import java.util.List;

public interface ICarteBancaire {
    public void createCarteBancaire(TypeCarte typeCarte, StatutCarte statutCarte);
    public List<CarteBancaire> getAllCartesBancaires();
    public CarteBancaire getCarteBancaireById(Long id);
    public void updateCarteBancaire(CarteBancaire carteBancaire);
    public List<StatutCarte> getAllStatutsCarte();
    public List<TypeCarte> getAllTypesCarte();
}
