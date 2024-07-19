package com.example.firebase_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {

    private TextView textResultado;
    private Button btnDeslogar, btnRedefinir;
    private FirebaseAuth auth;

    // Instanciando a classe que representa o perfil do usuário cadastrado no banco de dados
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        textResultado = findViewById(R.id.textResultado);
        btnDeslogar = findViewById(R.id.btnDeslogar);
        btnRedefinir = findViewById(R.id.btnRedefinir);

        // Instanciando a classe de autenticação do Firebase
        auth = FirebaseAuth.getInstance();

        // Recebendo o usuário atual baseado no processo de autenticação
        user = auth.getCurrentUser();

        // Método que irá exibir o email do usuário atual, assim que a tela iniciar
        ExibirEmail();

        btnDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Deslogar();
                IrParaLogin();
            }
        });

        btnRedefinir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IrParaRedefinirSenha();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Método para exibir o email do usuário atual
    public void ExibirEmail(){
        if(user == null){
            IrParaLogin();
        }
        else{
            textResultado.setText(user.getEmail()); // Alterando o texto do Textview e recebendo o email através de um método get
        }
    }

    // Método para o usuário deslogar sua conta da aplicação
    public void Deslogar(){
        FirebaseAuth.getInstance().signOut();
    }

    public void IrParaLogin(){
        Intent intent = new Intent(Home.this, Login.class);
        startActivity(intent);
        finish();
    }

    public void IrParaRedefinirSenha(){
        Intent intent = new Intent(Home.this, Nova_Senha.class);
        startActivity(intent);
        finish();
    }
}