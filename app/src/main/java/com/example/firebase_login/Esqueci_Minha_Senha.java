package com.example.firebase_login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuth;

public class Esqueci_Minha_Senha extends AppCompatActivity {

    private TextInputEditText editEmail;
    private Button btnEnviar;
    private ConstraintLayout layout;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_esqueci_minha_senha);

        editEmail = findViewById(R.id.editEmail);
        btnEnviar = findViewById(R.id.btnEnviar);
        layout = findViewById(R.id.layout);

        auth = FirebaseAuth.getInstance();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EsconderTeclado();
                if(ValidaCampo()){
                    EnviarEmail();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Método para iniciar o processo de envio de email de redefinição de senha ao usuário
    public void EnviarEmail(){
        String email = editEmail.getText().toString();

        // Método para enviar o e-mail de redefinição de senha, utilizando o email do usuário como parâmetro
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Email de redefinição de senha enviado!", Toast.LENGTH_LONG).show();
                            IrParaLogin();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Email não encontrado.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public boolean ValidaCampo(){
        if(editEmail.length() == 0){
            Toast.makeText(getApplicationContext(),"Preencha o campo de email!", Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            return true;
        }
    }

    public void IrParaLogin(){
        Intent intent = new Intent(Esqueci_Minha_Senha.this, Login.class);
        startActivity(intent);
    }

    public void EsconderTeclado(){
        InputMethodManager inm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
}