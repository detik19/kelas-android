package net.ruangtedy.android.latih.sqllite;

public class Contact {
	int _id;
	String _phone_number;
	String _name;
	
	
	public Contact() {
	}
	public Contact(int _id, String _name, String _phone_number) {
		this._id = _id;
		this._phone_number = _phone_number;
		this._name = _name;
	}
	public Contact(String _phone_number, String _name) {
		super();
		this._phone_number = _phone_number;
		this._name = _name;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String get_phone_number() {
		return _phone_number;
	}
	public void set_phone_number(String _phone_number) {
		this._phone_number = _phone_number;
	}
	public String get_name() {
		return _name;
	}
	public void set_name(String _name) {
		this._name = _name;
	}

}
