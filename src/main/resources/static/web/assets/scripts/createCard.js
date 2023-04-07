const { createApp } = Vue;

createApp({
  data() {
    return {
      cliente: [],
      selectedAccount: {},
      type: "",
      color:"",
    };
  },
  created() {
    this.loadData();
  },
  methods: {
    loadData: function () {
        axios.get("/api/clients/current")
            .then(response => {
                this.data = response.data;
                this.data.accounts.sort((a, b) => a.id - b.id);
                this.allCards = this.data.cards;
                this.cardType()
            })
            .catch(err => console.log(err));
    },
    logout: function () {
        axios.post('/api/logout')
            .then(response => {
                window.location.href = '/web/index.html';
            })
            .catch(error => {
                this.error = error.response.data.message;
            });
    },
    newCard: function () {
        axios.post('/api/clients/current/cards',`type=${this.cardType}&color=${this.cardColor}`)
            .then(response => {
                window.location.href = '/web/cards.html';
            })
            .catch(error => {
                this.error = error.response.data
                Swal.fire({
                  icon: 'error',
                  title: 'Oops...',
                  text: 'Max Cards reached!',
                })
                console.log(this.error)
            });
    },
  },
}).mount("#app");