package paixaoporti.com.br.fvg;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import Controller.CadastroControle;
import impl.CadastroDAO;

public class CadastroActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btSalvarCadastro;
    private EditText nomeCadastro, userCadastro, senhaCadastro;
    int codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_cadastro );

        nomeCadastro        = (EditText) findViewById(R.id.nameCadastro);
        userCadastro        = (EditText) findViewById(R.id.novo_user_nome);
        senhaCadastro       = (EditText) findViewById(R.id.novo_user_senha);

        btSalvarCadastro    = (Button) findViewById(R.id.cadastra_user);

        btSalvarCadastro.setOnClickListener(this);

    }

    public void salvarCadastro() {

        CadastroControle c = new CadastroControle();
        CadastroDAO dao = new CadastroDAO();

        c.setNome( nomeCadastro.getText().toString() );
        c.setUser( userCadastro.getText().toString() );
        c.setSenha( senhaCadastro.getText().toString() );
        dao.create( c );

        limparCampos();
    }

    public void validacao(){

        CadastroDAO dao = new CadastroDAO ();

        String nomeCadastroGet  = nomeCadastro.getText().toString();
        String userCadastroGet  = userCadastro.getText().toString();
        String senhaCadastroGet = senhaCadastro.getText().toString();

        if ( temConexao(CadastroActivity.this) == false ) {

            mostraAlerta();

        } else if ( nomeCadastroGet == null ||  nomeCadastroGet.equals("") ){

            nomeCadastro.setError( "Campo Obrigatorio!" );

        } else if( userCadastroGet == null || userCadastroGet.equals("") ){

            userCadastro.setError ( "Campo Obrigatorio!" );

        } else if( senhaCadastroGet == null || senhaCadastroGet.equals("") ) {

            senhaCadastro.setError ( "Campo Obrigatorio!" );

        }else if(dao.checkLogin(userCadastro.getText().toString (), senhaCadastro.getText().toString ())){

            Toast.makeText ( CadastroActivity.this, "Usuário já existe!!!", Toast.LENGTH_SHORT ).show ( );
            limparCampos ();

        } else {

            Intent f = new Intent(CadastroActivity.this,LoginActivity.class);
            startActivity(f);
            salvarCadastro ();
            limparCampos ();

            Toast.makeText(CadastroActivity.this, "Cadastro Salvo com sucesso!!!", Toast.LENGTH_SHORT).show();

            finish ();

        }
    }

    public void limparCampos() {
        nomeCadastro.setText("");
        userCadastro.setText("");
        senhaCadastro.setText("");
        this.codigo = 0;
    }

    // Se precisar desse método pra mais de uma classe, mude ele pra ser estático.
    private boolean temConexao(Context classe) {
        //Pego a conectividade do contexto passado como argumento
        ConnectivityManager gerenciador = (ConnectivityManager) classe.getSystemService( Context.CONNECTIVITY_SERVICE);
        //Crio a variável informacao que recebe as informações da Rede
        NetworkInfo informacao = gerenciador.getActiveNetworkInfo();
        //Se o objeto for nulo ou nao tem conectividade retorna false
        if ((informacao != null) && (informacao.isConnectedOrConnecting()) && (informacao.isAvailable())) {
            return true;
        }
        return false;
    }

    // Mostra a informação caso não tenha internet.
    private void mostraAlerta() {
        AlertDialog.Builder informa = new AlertDialog.Builder(CadastroActivity.this);
//        informa.setMessage("Sem conexão com a internet.");
//        informa.setNeutralButton("Voltar", null).show();
        showSettingsAlert();
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Internet desativado!");
        alertDialog.setMessage("Ativar Internet?");
        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent( Settings.ACTION_WIRELESS_SETTINGS );
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cadastra_user) {

            validacao();

        }

    }
}
