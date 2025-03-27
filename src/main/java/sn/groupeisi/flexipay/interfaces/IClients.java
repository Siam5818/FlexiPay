package sn.groupeisi.flexipay.interfaces;

import sn.groupeisi.flexipay.entities.Client;
import sn.groupeisi.flexipay.enums.StatutCarte;
import sn.groupeisi.flexipay.enums.TypeCarte;

import java.util.List;

public interface IClients {
    public void createClient(String name, String prenom, String adresse, String email, String telephone, TypeCarte typeCarte, StatutCarte statutCarte);
    List<Client> getAllClients();
    public List<Client> searchClientsByName(String name);
    public void deleteClient(Long id);
    public void updateClient(Long id, String name, String prenom, String adresse, String email, String telephone);
}
