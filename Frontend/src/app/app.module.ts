import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { LocalStorage, NgxWebstorageModule } from 'ngx-webstorage';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { CardComponent } from './card/card.component';
import { HomeComponent } from './home/home.component';
import { HeaderComponent } from './header/header.component';
import { UserMenuComponent } from './user-menu/user-menu.component';
import { LoginComponent } from './user/login/login.component';
import { SignUpComponent } from './user/sign-up/sign-up.component';

import { AngularMaterialModule } from './angular-material/angular-material.module';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ApiService } from './service/api.service';
import { AuthService } from './service/auth.service';
import { UserService } from './service/user.service';
import { ConfigService } from './service/config.service';

import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenInterceptor } from './interceptor/TokenInterceptor';
import { CommunityListComponent } from './community/community-list/community-list.component';
import { CommunityItemComponent } from './community/community-item/community-item.component';
import { CommunitiesComponent } from './community/communities/communities.component';
import { CreateCommunityComponent } from './community/create-community/create-community.component';
import { PostListComponent } from './post/post-list/post-list.component';
import { PostItemComponent } from './post/post-item/post-item.component';
import { RuleItemComponent } from './rule/rule-item/rule-item.component';
import { RuleListComponent } from './rule/rule-list/rule-list.component';
import { RulesComponent } from './rule/rules/rules.component';
import { FlairItemComponent } from './flair/flair-item/flair-item.component';
import { FlairListComponent } from './flair/flair-list/flair-list.component';
import { FlairsComponent } from './flair/flairs/flairs.component';
import { ViewCommunityComponent } from './community/view-community/view-community.component';
import { ViewPostComponent } from './post/view-post/view-post.component';
import { SuspendCommunityComponent } from './community/suspend-community/suspend-community.component';
import { UpdateCommunityComponent } from './community/update-community/update-community.component';
import { UpdatePostComponent } from './post/update-post/update-post.component';
import { CommentItemComponent } from './comment/comment-item/comment-item.component';
import { CommentListComponent } from './comment/comment-list/comment-list.component';
import { CommentsComponent } from './comment/comments/comments.component';
import { CreatePostComponent } from './post/create-post/create-post.component';
import { CreateCommentComponent } from './comment/create-comment/create-comment.component';
import { UpdateCommentComponent } from './comment/update-comment/update-comment.component';
import { ProfileComponent } from './user/profile/profile.component';
import { UpdateUserComponent } from './user/update-user/update-user.component';
import { UpdatePasswordComponent } from './user/update-password/update-password.component';

@NgModule({
  declarations: [
    AppComponent,
    CardComponent,
    HomeComponent,
    HeaderComponent,
    UserMenuComponent,
    LoginComponent,
    SignUpComponent,
    CommunityListComponent,
    CommunityItemComponent,
    CommunitiesComponent,
    CreateCommunityComponent,
    PostListComponent,
    PostItemComponent,
    RuleItemComponent,
    RuleListComponent,
    RulesComponent,
    FlairItemComponent,
    FlairListComponent,
    FlairsComponent,
    ViewCommunityComponent,
    ViewPostComponent,
    SuspendCommunityComponent,
    UpdateCommunityComponent,
    UpdatePostComponent,
    CommentItemComponent,
    CommentListComponent,
    CommentsComponent,
    CreatePostComponent,
    CreateCommentComponent,
    UpdateCommentComponent,
    ProfileComponent,
    UpdateUserComponent,
    UpdatePasswordComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    NoopAnimationsModule,
    AngularMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    NgxWebstorageModule.forRoot()
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    AuthService,
    ApiService,
    UserService,
    ConfigService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
