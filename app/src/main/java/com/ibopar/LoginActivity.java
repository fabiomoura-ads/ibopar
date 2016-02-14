package com.ibopar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ibopar.business.UsuarioBusiness;

/**
 * Created by Fabio Moura on 18/10/2015.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText login;
    private EditText senha;
    private Button btnLogar;
    private Button btnCadastrar;
    //private static final String LOG = LoginActivity.class.getSimpleName().toString();
    private static final String LOG = "TESTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.login = (EditText) (findViewById(R.id.edtLogin));
        this.senha = (EditText) (findViewById(R.id.edtSenha));

        this.btnLogar = (Button) (findViewById(R.id.btnLogar));
        this.btnLogar.setOnClickListener(logar);

        this.btnCadastrar = (Button) (findViewById(R.id.btnCadastrar));
        this.btnCadastrar.setOnClickListener(cadastrar);

        if ( this.getSharedPreferences("usuario", Context.MODE_PRIVATE).getLong("id", 0) != 0 ) {
            Intent intent = new Intent(LoginActivity.this, DetailActivity.class);
            //List<Programacao> lista = getIntent().getParcelableArrayListExtra("lista");
            //intent.putParcelableArrayListExtra("lista", (ArrayList<? extends Parcelable>) lista);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if ( id == android.R.id.home ) {
            finish();
        }

        return true;
    }

    View.OnClickListener logar = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.i(LOG, "Autenticando Usuário: Login: " + login.getText().toString() + ", senha: " + senha.getText().toString() );

            new UsuarioBusiness(LoginActivity.this).autenticarUsuario(login.getText().toString(), senha.getText().toString());

        }
    };

    View.OnClickListener cadastrar = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.i(LOG, "Cadastrando Usuário: Login: " + login.getText().toString() + ", senha: " + senha.getText().toString() );

            new UsuarioBusiness(LoginActivity.this).cadastrarUsuario(login.getText().toString(), senha.getText().toString());

        }
    };

}
