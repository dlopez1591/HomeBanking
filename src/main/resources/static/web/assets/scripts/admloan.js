const { createApp } = Vue;

createApp({
    data() {
        return {
            rest: undefined,
            clients: [],
            newClient: {
                firstName: "",
                lastName: "",
                email: "",
            },
            modiClient: {},
            maxAmount: "",
            name: "",
            percentage: "",
            payment: undefined,
            loans:"",
            loansPayments:[]
        }
    },
    created() {
        this.loadData();
    },
    methods: {
        addClient: function () {
            axios.post("/api/clients", this.newClient)
                .then(res => this.loadData())
                .catch(err => console.log(err));
        },
        loadData: function () {
            axios.get("/api/clients")
                .then(response => {
                    this.rest = response.data;
                    this.clients = response.data;
                    this.newClient.firstName = "";
                    this.newClient.lastName = "";
                    this.newClient.email = "";
                })
                .catch(err => console.log(err));
        },
        createLoan(){
            
            axios.post("/api/loans/admin"
            , {
                "name": this.name,
                "maxAmount": this.maxAmount,
                "payments": this.payOptions,
                "interest": this.interest
            },)
            .then(() => {
                console.log("success")
            })
        },
        deleteClient: function () {
            axios.delete(this.modiClient._links.self.href)
                .then(res => this.loadData())
                .catch(err => console.log(err));
        },
        logOut: function () {
            Swal.fire({
                title: 'Are you sure to go out?',
                showCancelButton: true,
                confirmButtonText: 'Save',
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/logout')
                        .then(response => {
                            Swal.fire('Logout successful!', '', 'success')
                            window.location.href = '/web/index.html';
                        })
                        .catch(error => {
                            this.error = error.response.data;
                        });
                }
            })
        },
        modify: function (client) {
            this.modiClient = { ...client };
        },
        modifyClient: function () {
            axios.put(this.modiClient._links.self.href, this.modiClient)
                .then(res => {
                    this.loadData();
                })
                .catch(err => console.log(err));
        },
        addNumberPayments(payment){
            this.loansPayments.push(payment)
        },
    }
}).mount('#app');