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

public class Login extends AppCompatActivity {

    private TextInputEditText editEmail, editSenha;
    private Button btnLogin, btnMudarTela;
    private ProgressBar progressBar;
    private ConstraintLayout layout;

    private FirebaseAuth auth;

    // Método que irá verificar se o usuário já está conectado, para então redirecioná-lo direto para a Home
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            IrParaHome();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        btnLogin = findViewById(R.id.btnLogin);
        btnMudarTela = findViewById(R.id.btnMudarTela);
        progressBar = findViewById(R.id.progressBar);
        layout = findViewById(R.id.layout);

        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EsconderTeclado();
                if(ValidaCampo()){
                    Logar();
                }
            }
        });

        btnMudarTela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrParaCadastro();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Método que irá iniciar o processo de login do usuário
    public void Logar(){

        progressBar.setVisibility(View.VISIBLE);

        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        // Método que irá realizar o login do usuário, utilizando o email e a senha como parâmetros
        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            // Caso os dados de login sejam compatíveis
                            Toast.makeText(Login.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                            IrParaHome();
                        }
                        else {
                            // Caso haja incoerência de dados
                            Toast.makeText(Login.this, "Falha na autenticação.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public boolean ValidaCampo(){
        if(editEmail.length() == 0 || editSenha.length() == 0){
            Toast.makeText(getApplicationContext(),"Preencha todos os campos!", Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            return true;
        }
    }

    public void IrParaCadastro(){
        Intent intent = new Intent(Login.this, Cadastro.class);
        startActivity(intent);
    }

    public void IrParaHome(){
        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
    }

    public void IrParaEsqueciSenha(View view){
        Intent intent = new Intent(Login.this, Esqueci_Minha_Senha.class);
        startActivity(intent);
    }

    public void EsconderTeclado(){
        InputMethodManager inm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
}