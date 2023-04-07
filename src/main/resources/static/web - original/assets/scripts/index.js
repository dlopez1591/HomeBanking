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
      axios.post('/api/login', `email=${this.email}&password=${this.password}`)
        .then(response => {
          this.email = '';
          this.password = '';
          window.location.href = '/web/accounts.html';
        })
        .catch(error => {
          console.log(error);
          this.error = error.response.data.message;
          Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Something went wrong!',0
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

