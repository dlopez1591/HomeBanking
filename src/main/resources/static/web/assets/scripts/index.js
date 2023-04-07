const { createApp } = Vue;

createApp({
  data() {
    return {
      email: '',
      password: '',
      error: '',
      errorNumber:'',
      firstName: '',
      lastName: '',
    }
  },
  methods: {
    login() {
      axios.post('/api/login', `email=${this.email}&password=${this.password}`, {
          headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
          }
      })
      .then(response => {
          if(this.email.includes("admin")){
              window.location.href = '/web/admloan.html';
          }
          else {
              window.location.href = '/web/accounts.html';
          }
      })
      .catch(error => {
          this.error = error.response.data.message;
          Swal.fire({
              icon: 'error',
              title: 'Error de informacion',
              text: 'Revisa los campos a completar',
          })
      });
    },
    register(){
      axios.post("/api/clients", `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`)
        .then(response => {
            this.login();
            this.email= '';
            this.password= '';
            this.error= '';
            this.errorNumber='';
            this.firstName= '';
            this.lastName= '';
        })
        .catch(error => {
            console.log(error);
            this.error = error.response.data.message;
        });
    },
  }
}).mount('#app');
