package sn.groupeisi.flexipay.interfaces;

import sn.groupeisi.flexipay.entities.Abonnement;
import sn.groupeisi.flexipay.enums.StatutAbonnement;

import java.util.List;

public interface IAbonnement {
    public List<Abonnement> getAllAbonnements();

    public Abonnement getAbonnementById(Long id) ;

    public Abonnement createAbonnement(String numcarte, String service, double montant, StatutAbonnement statutAbonnement);

    public Abonnement updateAbonnement(Abonnement abonnement);

    public void deleteAbonnement(Long id);
}
