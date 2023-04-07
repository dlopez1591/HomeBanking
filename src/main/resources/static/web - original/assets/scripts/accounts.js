const { createApp } = Vue;

createApp({
  data() {
    return {
      cliente: [],
      selectedAccount: {},
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
              this.cliente.accounts.sort((a, b) => a.id - b.id);
          })
          .catch(err => console.log(err));
  },
    openModal(account) {
      let creationDate = new Date(account.creationDate).toISOString();
      account.creationDate = creationDate.substr(0, 10);
      this.selectedAccount = account;
    },
    newAccount:function() {
      axios.post('/api/clients/current/accounts') 
          .then(response => {
              this.loadData();
          })
          .catch(error => {
              this.error = error.response.data.message;
          });
  },
  logout:function() {
    axios.post('/api/logout') 
        .then(response => {
            window.location.href = '/web/index.html';
        })
        .catch(error => {
            this.error = error.response.data.message;
        });
},
  },
}).mount("#app");
