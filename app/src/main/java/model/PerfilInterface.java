package model;

import java.util.List;

import controller.PerfilControle;

public interface PerfilInterface {

    void userFindById(String login, String senha);

    void update(PerfilControle obj);

    List <PerfilControle> getListaPerfil();

}
