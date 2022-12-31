import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CreateReactionDTO } from '../model/CreateReactionDTO';
import { Reaction } from '../model/reaction.model';

@Injectable({
  providedIn: 'root'
})
export class ReactionService {
  api = "http://localhost:8080/api/reactions"

  constructor(private http: HttpClient) { }

  create(reaction: CreateReactionDTO) {
    return this.http.post<Reaction>(this.api + "/create", reaction);
  }
}