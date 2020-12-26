import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserRegisterModel } from './UserRegisterModel';
import { UserLoginModel } from './UserLoginModel';

@Injectable({
  providedIn: 'root'
})
export class AuthService {


  private _registerUrl = "http://localhost:8080/data/all";
  private _loginUrl = "http://localhost:8080/User?";

  constructor(private _http:HttpClient) { }

  registerUser(user:UserRegisterModel){
    return this._http.post<any>(this._registerUrl,user);
  }

  loginUser(user:UserLoginModel){
    return this._http.post<any>(this._loginUrl,user);
  }

  loggedIn(){
    return !!localStorage.getItem('token');
  }

  getToken(){
    return localStorage.getItem('token');
  }

}
