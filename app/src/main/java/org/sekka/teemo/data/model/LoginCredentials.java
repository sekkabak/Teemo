package org.sekka.teemo.data.model;

public class LoginCredentials {
    int _id;
    String _name;
    String _passwd;
    boolean _loginWithBiometrics;

    public LoginCredentials() {
    }

    public LoginCredentials(int _id, String _name, String _passwd, boolean _loginWithBiometrics) {
        this._id = _id;
        this._name = _name;
        this._passwd = _passwd;
        this._loginWithBiometrics = _loginWithBiometrics;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_passwd(String _passwd) {
        this._passwd = _passwd;
    }

    public String get_name() {
        return _name;
    }

    public String get_passwd() {
        return _passwd;
    }

    public boolean is_loginWithBiometrics() {
        return _loginWithBiometrics;
    }

    public void set_loginWithBiometrics(boolean _loginWithBiometrics) {
        this._loginWithBiometrics = _loginWithBiometrics;
    }
}
