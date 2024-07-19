package com.example.firebase_login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Cadastro extends AppCompatActivity {

    // Instanciando as classes dos elementos da interface
    private TextInputEditText editEmail, editSenha;
    private Button btnCadastrar, btnMudarTela;
    private ProgressBar progressBar;

    // Instanciando o layout para podermos esconder o teclado
    private ConstraintLayout layout;

    // Instanciando a classe de autenticação do Firebase
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        // Referenciando os objetos criados através dos IDs dos elementos
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnMudarTela = findViewById(R.id.btnMudarTela);
        progressBar = findViewById(R.id.progressBar);
        layout = findViewById(R.id.layout);

        // Obtendo uma instância para o objeto de autenticação criado; desta forma poderemos utilizar seus métodos, como o de cadastro
        auth = FirebaseAuth.getInstance();

        // Adicionando listeners para os botões
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EsconderTeclado();
                if(ValidaCampo()){ // Se os campos estiverem preenchidos corretamente, podemos realizar o cadastro
                    Cadastrar();
                }
            }
        });

        btnMudarTela.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                IrParaLogin();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Método para INICIAR o processo de cadastro do usuário
    public void Cadastrar(){

        // A progressBar irá ficar visível quando o método inicia, para evidenciar o carregamento do processo
        progressBar.setVisibility(View.VISIBLE);

        // Variáveis recebendo os valores dos TextInputs
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        // Método que irá realizar o cadastro do usuário, utilizando o email e a senha como parâmetros
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressBar.setVisibility(View.GONE);

                        // Caso o cadastro seja realizado com sucesso
                        if (task.isSuccessful()) {
                            Toast.makeText(Cadastro.this, "Cadastro realizado com êxito!", Toast.LENGTH_SHORT).show();
                            IrParaHome();
                        }

                        // Caso o email já esteja cadastro, ou não esteja em um formato válido
                        else {
                            Toast.makeText(Cadastro.this, "Falha ao realizar cadastro. Tente novamente.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Método para verificar os campos de TextInput
    public boolean ValidaCampo(){

        boolean res = true;

        if(editEmail.length() == 0 || editSenha.length() == 0){
            Toast.makeText(getApplicationContext(),"Preencha todos os campos!", Toast.LENGTH_LONG).show();
            res = false;
        }
        else if(editSenha.length() > 0 && editSenha.length() < 6){
            Toast.makeText(getApplicationContext(),"Nova senha muito fraca!", Toast.LENGTH_LONG).show();
            res = false;
        }

        return res;
    }

    // Método para ir até a tela tela de Login, utilizando a classe Intent
    public void IrParaLogin(){
        Intent intent = new Intent(Cadastro.this, Login.class);
        startActivity(intent);
    }

    // Método para ir até a tela de Home, caso o cadastro seja realizado
    public void IrParaHome(){
        Intent intent = new Intent(Cadastro.this, Home.class);
        startActivity(intent);
    }

    // Método para esconder o teclado após o clique de um botão, utilizando o InputMethodManager
    public void EsconderTeclado(){
        InputMethodManager inm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
}