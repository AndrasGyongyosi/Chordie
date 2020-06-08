import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { SocialLoginModule, AuthServiceConfig, GoogleLoginProvider } from 'angularx-social-login';
import { TooltipModule } from 'ng2-tooltip-directive';
import { MenuComponent } from './components/main-components/menu/menu.component';
import { HomeComponent } from './components/main-components/home/home.component';
import { AboutComponent } from './components/main-components/about/about.component';
import { InstrumentsAndListsComponent } from './components/main-components/instruments-and-lists/instruments-and-lists.component';
import { InstrumentAndChordSelectorComponent } from './components/main-components/home/instrument-and-chord-selector/instrument-and-chord-selector.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { ChordsComponent } from './components/main-components/chords/chords.component';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatDialogModule } from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CatchTipDialogComponent } from './components/dialogs/catch-tip-dialog/catch-tip-dialog.component';
import { EditInstrumentDialogComponent } from './components/dialogs/edit-instrument-dialog/edit-instrument-dialog.component';
import { AddNewListDialogComponent } from './components/dialogs/add-new-list-dialog/add-new-list-dialog.component';
import { AngularResizedEventModule } from 'angular-resize-event';
import {ScrollingModule} from '@angular/cdk/scrolling';

const config = new AuthServiceConfig([
  {
    id: GoogleLoginProvider.PROVIDER_ID,
    provider: new GoogleLoginProvider("902904919760-8uea185c8i6meeap7gga8lhjivgtt33t.apps.googleusercontent.com")
  }
]);
export function provideConfig() {
  return config;
}

@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    HomeComponent,
    AboutComponent,
    InstrumentsAndListsComponent,
    EditInstrumentDialogComponent,
    InstrumentAndChordSelectorComponent,
    ChordsComponent,
    CatchTipDialogComponent,
    AddNewListDialogComponent
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    SocialLoginModule,
    TooltipModule,
    NgbModule,
    FormsModule,
    MatExpansionModule,
    BrowserAnimationsModule,
    MatDialogModule,
    AngularResizedEventModule,
    ScrollingModule
  ],
  providers: [
    {
      provide: AuthServiceConfig,
      useFactory: provideConfig
    }
  ],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    EditInstrumentDialogComponent,
    CatchTipDialogComponent,
    AddNewListDialogComponent
  ]
})
export class AppModule { }
