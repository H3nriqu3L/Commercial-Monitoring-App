package com.example.commercial_monitoring_app;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.commercial_monitoring_app.database.DatabaseHelper;
import com.example.commercial_monitoring_app.model.Client;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddClientActivity extends AppCompatActivity {

    private TextInputLayout emailLayout;
    private TextInputEditText emailEditText;
    private TextInputLayout cpfLayout;
    private TextInputEditText cpfEditText;
    private TextInputLayout birthLayout;
    private TextInputEditText birthEditText;
    private TextInputLayout phoneLayout;
    private TextInputEditText phoneEditText;
    private TextInputLayout nameLayout;
    private TextInputEditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        try {
            initializeViews();
            setupValidation();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao inicializar a tela: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initializeViews() {
        emailLayout = findViewById(R.id.email_layout);
        emailEditText = findViewById(R.id.edit_email);
        cpfLayout = findViewById(R.id.cpf_layout);
        cpfEditText = findViewById(R.id.edit_cpf);
        birthLayout = findViewById(R.id.dataNascimento_layout);
        birthEditText = findViewById(R.id.edit_dataNascimento);
        phoneLayout = findViewById(R.id.telefone_layout);
        phoneEditText = findViewById(R.id.edit_telefone);
        nameLayout = findViewById(R.id.nome_layout);
        nameEditText = findViewById(R.id.edit_nome);


        if (emailLayout == null || emailEditText == null ||
                cpfLayout == null || cpfEditText == null ||
                birthLayout == null || birthEditText == null ||
                phoneLayout == null || phoneEditText == null ||
                nameLayout == null || nameEditText == null) {
            throw new RuntimeException("One or more views not found.");
        }
    }

    private void setupValidation() {
        // Add text watchers for real-time validation
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateName();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        cpfEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCPF();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        birthEditText.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private String previous = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;

                String current = s.toString();

                // Remove all non-digit
                String digits = current.replaceAll("[^\\d]", "");


                StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < digits.length() && i < 8; i++) {
                    if (i == 2 || i == 4) {
                        formatted.append("/");
                    }
                    formatted.append(digits.charAt(i));
                }

                String newText = formatted.toString();


                if (!newText.equals(current)) {
                    isUpdating = true;
                    birthEditText.setText(newText);
                    birthEditText.setSelection(newText.length());
                    isUpdating = false;
                }

                previous = newText;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isUpdating) {
                    validateBirthDate();
                }
            }
        });

        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePhone();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private boolean validateEmail() {
        try {
            Editable emailText = emailEditText.getText();
            String email = emailText != null ? emailText.toString().trim() : "";

            if (email.isEmpty()) {
                emailLayout.setError("E-mail é obrigatório");
                return false;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailLayout.setError("E-mail inválido");
                return false;
            }

            emailLayout.setError(null);
            return true;
        } catch (Exception e) {
            emailLayout.setError("Erro na validação do e-mail");
            return false;
        }
    }

    private boolean validateName() {
        try {
            Editable nameText = nameEditText.getText();
            String name = nameText != null ? nameText.toString().trim() : "";

            if (name.isEmpty()) {
                nameLayout.setError("Nome é obrigatório");
                return false;
            }

            // Check if name has at least 2 characters
            if (name.length() < 2) {
                nameLayout.setError("Nome deve ter pelo menos 2 caracteres");
                return false;
            }

            // Check if name contains only letters and spaces
            if (!name.matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
                nameLayout.setError("Nome deve conter apenas letras e espaços");
                return false;
            }

            nameLayout.setError(null);
            return true;
        } catch (Exception e) {
            nameLayout.setError("Erro na validação do nome");
            return false;
        }
    }

    private boolean validateCPF() {
        try {
            Editable cpfText = cpfEditText.getText();
            String cpf = cpfText != null ? cpfText.toString().trim().replaceAll("[^0-9]", "") : "";

            if (cpf.isEmpty()) {
                cpfLayout.setError("CPF é obrigatório");
                return false;
            }

            if (!isValidCPF(cpf)) {
                cpfLayout.setError("CPF inválido");
                return false;
            }

            cpfLayout.setError(null);
            return true;
        } catch (Exception e) {
            cpfLayout.setError("Erro na validação do CPF");
            return false;
        }
    }

    private boolean validateBirthDate() {
        try {
            Editable birthText = birthEditText.getText();
            String birthDate = birthText != null ? birthText.toString().trim() : "";

            if (birthDate.isEmpty()) {
                birthLayout.setError("Data de nascimento é obrigatória");
                return false;
            }

            if (!isValidBirthDate(birthDate)) {
                birthLayout.setError("Data inválida (use dd/mm/aaaa)");
                return false;
            }

            birthLayout.setError(null);
            return true;
        } catch (Exception e) {
            birthLayout.setError("Erro na validação da data");
            return false;
        }
    }

    private boolean validatePhone() {
        try {
            Editable phoneText = phoneEditText.getText();
            String phone = phoneText != null ? phoneText.toString().trim().replaceAll("[^0-9]", "") : "";

            if (phone.isEmpty()) {
                phoneLayout.setError("Telefone é obrigatório");
                return false;
            }

            if (!isValidPhone(phone)) {
                phoneLayout.setError("Telefone inválido");
                return false;
            }

            phoneLayout.setError(null);
            return true;
        } catch (Exception e) {
            phoneLayout.setError("Erro na validação do telefone");
            return false;
        }
    }

    private boolean isValidCPF(String cpf) {
        try {
            // Remove any non-digit characters
            cpf = cpf.replaceAll("[^0-9]", "");

            // Check if CPF has 11 digits
            if (cpf.length() != 11) {
                return false;
            }

            // Check for known invalid CPFs (all same digits)
            if (cpf.matches("(\\d)\\1{10}")) {
                return false;
            }

            // Calculate first verification digit
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int firstDigit = 11 - (sum % 11);
            if (firstDigit >= 10) firstDigit = 0;

            // Calculate second verification digit
            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int secondDigit = 11 - (sum % 11);
            if (secondDigit >= 10) secondDigit = 0;

            // Verify both digits
            return Character.getNumericValue(cpf.charAt(9)) == firstDigit &&
                    Character.getNumericValue(cpf.charAt(10)) == secondDigit;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidBirthDate(String date) {
        try {
            // Expected format: dd/mm/yyyy
            if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
                return false;
            }

            String[] parts = date.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            // Basic range checks
            if (month < 1 || month > 12) return false;
            if (day < 1 || day > 31) return false;

            // Year range check (reasonable birth years)
            Calendar now = Calendar.getInstance();
            int currentYear = now.get(Calendar.YEAR);
            if (year < 1900 || year > currentYear) return false;

            // Create calendar to validate the date
            Calendar cal = Calendar.getInstance();
            cal.setLenient(false); // This will throw exception for invalid dates
            cal.set(year, month - 1, day); // Month is 0-based in Calendar
            cal.getTime(); // This triggers validation

            // Check if the person is not in the future
            Calendar birthCal = Calendar.getInstance();
            birthCal.set(year, month - 1, day);
            return !birthCal.after(now);

        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidPhone(String phone) {
        try {
            // Remove any non-digit characters
            phone = phone.replaceAll("[^0-9]", "");

            // With country code 55
            if (phone.startsWith("55")) {
                phone = phone.substring(2);
            }

            // Check length (10 for landline, 11 for mobile)
            if (phone.length() != 10 && phone.length() != 11) {
                return false;
            }

            // Check if area code is valid (11-99)
            String areaCode = phone.substring(0, 2);
            int area = Integer.parseInt(areaCode);
            if (area < 11 || area > 99) {
                return false;
            }

            // For mobile numbers (11 digits), the third digit should be 9
            if (phone.length() == 11) {
                return phone.charAt(2) == '9';
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validateAllFields() {
        boolean isNameValid = validateName();
        boolean isEmailValid = validateEmail();
        boolean isCpfValid = validateCPF();
        boolean isBirthValid = validateBirthDate();
        boolean isPhoneValid = validatePhone();

        return isEmailValid && isCpfValid && isBirthValid && isPhoneValid && isNameValid;
    }

    public void navigateBack(View view) {
        finish();
    }

    public void saveClient(View view) {
        try {
            if (validateAllFields()) {
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String cpf = cpfEditText.getText().toString().trim();
                String birthDate = birthEditText.getText().toString().trim();

                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("email", email);
                values.put("phoneNumber", phone);
                values.put("cpf", cpf);
                values.put("birthDate", birthDate);


                long id = DatabaseHelper.getInstance().insert("Client", values);

                if (id != -1) {

                    Client newClient = new Client(name, email, phone, cpf, birthDate);

                    MyApp.getClientList().add(newClient);

                    Toast.makeText(this, "Cliente salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    Log.d("AddClientActivity", "Client added. Total clients: " + MyApp.getClientList().size());

                    Log.d("AddClientActivity", "About to call setResult(RESULT_OK)");
                    Log.d("AddClientActivity", "RESULT_OK value is: " + RESULT_OK);

                    setResult(RESULT_OK);

                    Log.d("AddClientActivity", "Called setResult, now finishing activity");
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao salvar cliente no banco de dados.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Por favor, corrija os erros antes de salvar", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao salvar cliente: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}