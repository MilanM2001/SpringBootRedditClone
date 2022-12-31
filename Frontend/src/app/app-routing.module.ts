import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UpdateCommentComponent } from './comment/update-comment/update-comment.component';
import { CommunitiesComponent } from './community/communities/communities.component';
import { CreateCommunityComponent } from './community/create-community/create-community.component';
import { SuspendCommunityComponent } from './community/suspend-community/suspend-community.component';
import { UpdateCommunityComponent } from './community/update-community/update-community.component';
import { ViewCommunityComponent } from './community/view-community/view-community.component';


import { HomeComponent } from './home/home.component';
import { CreatePostComponent } from './post/create-post/create-post.component';
import { UpdatePostComponent } from './post/update-post/update-post.component';
import { ViewPostComponent } from './post/view-post/view-post.component';
import { LoginComponent } from './user/login/login.component';
import { ProfileComponent } from './user/profile/profile.component';
import { SignUpComponent } from './user/sign-up/sign-up.component';
import { UpdatePasswordComponent } from './user/update-password/update-password.component';
import { UpdateUserComponent } from './user/update-user/update-user.component';

const routes: Routes = [
  {
    path: 'mainPage',
    component: HomeComponent,
  },
  {
    path: 'view-post/:post_id',
    component: ViewPostComponent
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'signup',
    component: SignUpComponent,
  },
  {
    path: 'communities',
    component: CommunitiesComponent
  },
  {
    path: 'communities/add',
    component: CreateCommunityComponent
  },
  {
    path: 'view-community/:community_id',
    component: ViewCommunityComponent
  },
  {
    path: 'suspend-community/:community_id',
    component: SuspendCommunityComponent
  },
  {
    path: 'update-community/:community_id',
    component: UpdateCommunityComponent
  },
  {
    path: 'update-post/:post_id',
    component: UpdatePostComponent
  },
  {
    path: 'view-community/:community_id/posts/add',
    component: CreatePostComponent
  },
  {
    path: 'update-comment/:comment_id',
    component: UpdateCommentComponent
  },
  {
    path: 'profile/:user_id',
    component: ProfileComponent
  },
  {
    path: 'update-user/:user_id',
    component: UpdateUserComponent
  },
  {
    path: 'update-password/:user_id',
    component: UpdatePasswordComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
