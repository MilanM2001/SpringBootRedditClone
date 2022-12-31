import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Flair } from "../model/flair.model";

@Injectable({
    providedIn: 'root'
})
export class FlairService {
    api = "http://localhost:8080/api/"

    constructor(private http: HttpClient) { }

    getAll(): Observable<Array<Flair>> {
        return this.http.get<Array<Flair>>(this.api + "flairs/all");
    }


}