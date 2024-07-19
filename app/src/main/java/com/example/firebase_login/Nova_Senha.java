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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Nova_Senha extends AppCompatActivity {

    private TextInputEditText editSenhaAtual, editNovaSenha, editConfirmaSenha;
    private Button btnRedefinir;
    private ProgressBar progressBar;
    private ConstraintLayout layout;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nova_senha);

        editSenhaAtual = findViewById(R.id.editSenhaAtual);
        editNovaSenha = findViewById(R.id.editNovaSenha);
        editConfirmaSenha = findViewById(R.id.editConfirmaSenha);
        btnRedefinir = findViewById(R.id.btnRedefinir);
        progressBar = findViewById(R.id.progressBar);
        layout = findViewById(R.id.layout);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        btnRedefinir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EsconderTeclado();
                ReautenticarUsuario();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // O Método abaixo irá iniciar o processo de reautenticação do usuário atual. É preferível que realizemos
    // a reautenticação antes de permitir a renovação de senha, pois uma ação como essa pode apresentar riscos à segurança.
    // Desta forma, solicitamos a senha atual do usuário junta à nova senha desejada.
    public void ReautenticarUsuario(){

        if(ValidaCampo()){ // Verificando se os campos estão preenchidos antes de validar a senha atual

            progressBar.setVisibility(View.VISIBLE);

            String email = user.getEmail(); // Variável que está recebendo o email do usuário através do método getEmail();
            String senhaAtual = editSenhaAtual.getText().toString(); // Variável que está recebendo a senha atual pelo TextInput

            // Criando um objeto de credencial para o usuário, utilizando seu email e senha atual como parâmetros
            AuthCredential credential = EmailAuthProvider.getCredential(email, senhaAtual);

            // Método que irá reautenciar o usuário através da credencial criada
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            // Caso a credencial seja válida (caso a senha atual esteja correta), poderemos redefinir a senha
                            if(task.isSuccessful()){
                                RedefinirSenha();
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Senha atual incorreta!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    // Método que irá iniciar o processo de redefinição de senha
    public void RedefinirSenha(){

        String novaSenha = editNovaSenha.getText().toString();

        // método que irá realizar a redefinição da senha do usuário
        user.updatePassword(novaSenha)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            Toast.makeText(Nova_Senha.this, "Senha atualizada com êxito!", Toast.LENGTH_SHORT).show();
                            Deslogar();
                            IrParaLogin();
                        }
                    }
                });
    }

    public boolean ValidaCampo(){

        boolean res = true;

        if(editNovaSenha.length() == 0 || editSenhaAtual.length() == 0 || editConfirmaSenha.length() == 0){
            Toast.makeText(getApplicationContext(),"Preencha todos os campos!", Toast.LENGTH_LONG).show();
            res = false;
        }
        else if(editNovaSenha.length() > 0 && editNovaSenha.length() < 6){
            Toast.makeText(getApplicationContext(),"Nova senha muito fraca!", Toast.LENGTH_LONG).show();
            res = false;
        }
        // A nova senha deve ser diferente da atual
        else if(editNovaSenha.getText().toString().equals(editSenhaAtual.getText().toString())){
            Toast.makeText(getApplicationContext(),"Nova senha deve ser diferente da atual!", Toast.LENGTH_LONG).show();
            res = false;
        }
        // A confirmação de senha deve ser a mesma que a nova senha
        else if(!editConfirmaSenha.getText().toString().equals(editNovaSenha.getText().toString())){
            Toast.makeText(getApplicationContext(),"Confirmação de senha incorreta!", Toast.LENGTH_LONG).show();
            res = false;
        }

        return res;
    }

    public void Deslogar(){
        FirebaseAuth.getInstance().signOut();
    }

    public void IrParaLogin(){
        Intent intent = new Intent(Nova_Senha.this, Login.class);
        startActivity(intent);
    }

    public void EsconderTeclado(){
        InputMethodManager inm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
}