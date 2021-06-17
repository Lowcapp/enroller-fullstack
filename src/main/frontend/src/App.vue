<template>
  <div id="app">
    <h1>
      <img src="./assets/logo.svg" alt="Enroller" class="logo">
      System do zapisów na zajęcia
    </h1>
    <div v-if="authenticatedUsername">
      <h2>Witaj {{ authenticatedUsername }}!
        <a @click="logout()" class="float-right  button-outline button">Wyloguj</a>
      </h2>
      <meetings-page :username="authenticatedUsername"></meetings-page>
    </div>
    <div v-else>
		<button :class="registering ? 'button button-outline' : ''" @click="registering = false">Loguje się</button>
		<button :class="!registering ? 'button button-outline' : ''" @click="registering = true">Rejestruję się</button>
      <div v-if="errorMessage" class ="alert-warning">{{ errorMessage }}</div>
	  
	  <login-form v-if ="!registering" @login="login($event)"></login-form>
	  <login-form v-else @login="register($event)" button-label="Zarejestruj się"></login-form>
	  
    </div>
  </div>
</template>

<script>
    import "milligram";
    import LoginForm from "./LoginForm";
    import MeetingsPage from "./meetings/MeetingsPage";

    export default {
        components: {LoginForm, MeetingsPage},
        data() {
            return {
                authenticatedUsername: "",
				registering: false,
				errorMessage: ''
            };
        },
        methods: {
            login(user) {
                this.authenticatedUsername = user.login;
				this.errorMessage = '';
            },
			register(user) {
			   this.$http.post('participants', user)
					.then(response => {
					this.registering = false;
					this.errorMessage = '';
			})
			.catch(response => {
				this.errorMessage = 'Nazwa użytkownika jest zajęta';   
     }); 
			},
            logout() {
                this.authenticatedUsername = '';
            }
        }
    };
</script>

<style>
  #app {
    max-width: 1000px;
    margin: 0 auto;
  }

  .logo {
    vertical-align: middle;
  }
  
  .alert-warning {
  border: 3px red dotted;
  paddind: 3 px;
  text-align: center;
  background: pink;
  }
</style>

