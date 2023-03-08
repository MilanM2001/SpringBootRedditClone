import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommunitiesComponent } from './components/community/communities/communities.component';
import { CommunityAddComponent } from './components/community/community-add/community-add.component';
import { CommunityEditComponent } from './components/community/community-edit/community-edit.component';
import { CommunitySuspendComponent } from './components/community/community-suspend/community-suspend.component';
import { CommunityViewComponent } from './components/community/community-view/community-view.component';
import { LoginComponent } from './components/login/login.component';
import { MainPageComponent } from './components/main-page/main-page.component';

const routes: Routes = [
  {
    path: "Login",
    component: LoginComponent
  },
  {
    path: "Main-Page",
    component: MainPageComponent
  },
  {
    path: 'Communities',
    component: CommunitiesComponent
  },
  {
    path: 'community-view/:communityId',
    component: CommunityViewComponent
  },
  {
    path: 'community-edit/:communityId',
    component: CommunityEditComponent
  },
  {
    path: 'community-suspend/:communityId',
    component: CommunitySuspendComponent
  },
  {
    path: 'community/add',
    component: CommunityAddComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
