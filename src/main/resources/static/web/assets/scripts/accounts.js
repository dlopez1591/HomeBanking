const { createApp } = Vue;

createApp({
  data() {
    return {
      cliente: [],
      selectedAccount: {},
      startDate: '',
      endDate: '',
      activeAccount: undefined,
      accountsSort: undefined, 
      hideAccount: false,
      accountType:"",
    };
  },
  created() {
    this.loadData();
  },
  methods: {
    loadData: function () {
      axios.get("/api/clients/current")
          .then(response => {
              this.cliente = response.data;
              this.accountsSort= this.cliente.accounts.sort((a, b) => a.id - b.id);
              this.activeAccount = this.accountsSort.filter(account => account.show == true) ;          
          })
          .catch(err => console.log(err));
  },
    openModal(account) {
      let creationDate = new Date(account.creationDate).toISOString();
      account.creationDate = creationDate.substr(0, 10);
      
      this.selectedAccount = account;
    },
  logOut:function() {
    axios.post('/api/logout') 
        .then(response => {
            window.location.href = '/web/index.html';
        })
        .catch(error => {
            this.error = error.response.data.message;
        });
},
generatePdf: function() {
  axios.post('/api/clients/current/accounts/transactions/pdf', {startDate: this.startDate, endDate: this.endDate}, {responseType: 'blob'})
    .then(response => {
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'transactions.pdf');
      document.body.appendChild(link);
      link.click();
    })
    .catch(error => {
      console.log(error);
    });
  },
  deleteAcc(idAccount) {
    axios.patch(`/api/clients/current/accounts/delete/`+ idAccount)
        .then(response => {
            window.location.href = '/web/accounts.html';
        })
        .catch(error => {
            console.log(error)
            this.error = error.response.data.message; 
            console.log(error.response.data)
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: error.response.data

              })
        });
  },
  alertCreateAccount(){
    Swal.fire({
        title: '¿Que tipo de Cuenta deseas?',
        showDenyButton: true,
        showCloseButton: true,
        confirmButtonText: 'Cuenta Corriente',
        denyButtonText: 'Cuenta Ahorro'
      }).then((result) => {
        if (result.isConfirmed) {
            this.accountType = "CORRIENTE"
            this.newAccount()
        } else if (result.isDenied) {
            this.accountType = "AHORRO"
            this.newAccount() 
        }
      })
},
newAccount(){
  console.log("newAcc")
  axios.post('/api/clients/current/accounts', `accountType=${this.accountType}`) 
      .then(response => {
          console.log("holaa2")
          window.location.href = '/web/accounts.html';
      })
      .catch(error => {
          this.error = error.response.data.message; 
          Swal.fire({
              icon: 'error',
              title: 'Oops...',
              text: this.error

            })
      });
},
printPdf:function(){
  const element=document.querySelector(".print-content");
  let opt = {
      margin: 0.5,
      filename: 'TransactionsSummary.pdf',
      image: { type: 'jpeg', quality: 0.98 },
      html2canvas: { scale: 1 },
      jsPDF: { unit: 'in', format: 'letter', orientation: 'portrait' }
  };
  html2pdf().set(opt).from(element).save();
},
  },
}).mount("#app");