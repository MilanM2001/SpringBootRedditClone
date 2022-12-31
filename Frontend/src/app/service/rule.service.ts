import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Rule } from "../model/rule.model";

@Injectable({
    providedIn: 'root'
})
export class RuleService {
    api = "http://localhost:8080/api/"

    constructor(private http: HttpClient) { }

    getAll(): Observable<Array<Rule>> {
        return this.http.get<Array<Rule>>(this.api + "rules/all");
    }


}