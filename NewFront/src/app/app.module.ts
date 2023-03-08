import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { MatCardModule } from '@angular/material/card';
import { MatButtonModule} from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatToolbarModule } from '@angular/material/toolbar';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { AuthInterceptor } from './services/auth.interceptor';
import { HeaderComponent } from './components/header/header.component';
import { MainPageComponent } from './components/main-page/main-page.component';
import { CommunityItemComponent } from './components/community/community-item/community-item.component';
import { CommunityListComponent } from './components/community/community-list/community-list.component';
import { CommunityViewComponent } from './components/community/community-view/community-view.component';
import { CommunityAddComponent } from './components/community/community-add/community-add.component';
import { CommunityEditComponent } from './components/community/community-edit/community-edit.component';
import { CommunitiesComponent } from './components/community/communities/communities.component';
import { CommunitySuspendComponent } from './components/community/community-suspend/community-suspend.component';
import { PostAddComponent } from './components/post/post-add/post-add.component';
import { PostEditComponent } from './components/post/post-edit/post-edit.component';
import { PostItemComponent } from './components/post/post-item/post-item.component';
import { PostListComponent } from './components/post/post-list/post-list.component';
import { PostViewComponent } from './components/post/post-view/post-view.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HeaderComponent,
    MainPageComponent,
    CommunityItemComponent,
    CommunityListComponent,
    CommunityViewComponent,
    CommunityAddComponent,
    CommunityEditComponent,
    CommunitiesComponent,
    CommunitySuspendComponent,
    PostAddComponent,
    PostEditComponent,
    PostItemComponent,
    PostListComponent,
    PostViewComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatMenuModule,
    MatToolbarModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
