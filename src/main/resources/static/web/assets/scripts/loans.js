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
    loadData() {
      let stringUrl = location.search;
      let parameter = new URLSearchParams(stringUrl);
      let id = parameter.get("id");
      axios
        .get("/api/clients/current")
        .then((response) => {
          this.cliente = response.data;
        })
        .catch((error) => console.log("Something went wrong"));
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
  },
}).mount("#app");