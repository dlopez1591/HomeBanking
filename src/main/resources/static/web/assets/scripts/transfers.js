const { createApp } = Vue;
createApp({
    data() {
        return {
            data: [],
            numAccountDestini:"",
            numAccountOrigin:"",
            description:"",
            amount:"",
            show:"",
            accounts:[],
        }
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
        newTransfer:function(){
            axios.post('/api/transactions',`amount=${this.amount}&description=${this.description}&numAccountOrigin=${this.numAccountOrigin}&numAccountDestini=${this.numAccountDestini}`)
            .then(response=>{
                Swal.fire('transferencia con exito')
            })
            .catch(error=>{
                console.log(error);
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: error.response.data,
                })
            })
        },
        filterAccounts:function(){
            this.accounts=this.data.accounts.filter(account=>account.number!=this.numAccountOrigin);
        },
        navShow: function (value) {
            this.navbar = value;
        },
        beforeDestroy: function () {
            window.removeEventListener("resize", this.updateScreenSize);
        },
        updateScreenSize: function () {
            this.title = window.innerWidth > 500;
        },
    },
    mounted(){
        this.updateScreenSize();
        window.addEventListener("resize", this.updateScreenSize);
    },
}).mount('#app');