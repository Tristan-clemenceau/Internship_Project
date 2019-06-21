$(document).ready(function(){
	/*BUTTON*/
	 $btn_ajouter = $("#btn_ajouter");
     $btn_annuler = $("#btn_annuler");
	/*FIELD*/
     $field_alert_title = $("#alertTitle");
     $field_alert_content = $("#registerInputUsername");
	/*LABEL*/
	$lbl_username = $("#lbl_username");
	$lbl_footer_username = $("#footer_lbl_username");
	
	/*CODE*/
	setDefaultEnv();
	
    /*CANVAS*/
    /*CANVAS BAR*/
    var yo = document.getElementById('myChart').getContext('2d');
    var myChart = new Chart(yo, {
            type: 'bar',
                data: {
                    labels: ['2018', '2019'],
                    datasets: [{
                     label: 'Utilisateurs',
                        data: [45, 500],
                        backgroundColor: 
                            'rgba(255, 99, 132, 0.2)',
                        borderColor:
                            'rgba(255, 99, 132, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: 'Nombre d\'Utilisateur'
                },
                    scales: {
                        yAxes: [{
                            ticks: {
                                beginAtZero: true
                            }
                        }]
                    }
                }
    });
    /*CANVAS DOUGHNUT*/
    var yo2 = document.getElementById('myChart2').getContext('2d');
    var myChart2 = new Chart(yo2, {
            type: 'doughnut',
            data: {
                datasets: [{
                    data: [
                        17,
                        5,
                        10
                    ],
                    backgroundColor: [
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(153, 102, 255, 0.2)'
                    ],
                    label: 'Dataset 1',
                    borderColor: [
                            'rgba(54, 162, 235, 1)',
                            'rgba(255, 99, 132, 1)',
                            'rgba(153, 102, 255, 1)'
                        ],
                        borderWidth: 1
                }],
                labels: [
                    'Enfant',
                    'Adulte',
                    'Personne Agées'
                ]
            },
            options: {
                responsive: true,
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: 'Nombre de ticket par catégorie'
                },
                animation: {
                    animateScale: true,
                    animateRotate: true
                }
            }
        });
    /*JQGRID 1*/
    $("#jqGrid").jqGrid({
                url: 'data.json',//url de la cible
                datatype: "json",
                colModel: [
                    { label: 'Member Id', name: 'CategoryName', width: 75, editable: true },
                    { label: 'Member date register', name: 'ProductName', width: 90, editable: true },
                    { label: 'Member email', name: 'Country', width: 100, sortable: false, editable: true }                  
                ],
                loadonce: true,
                altRows : true,
                //rownumbers : true,
                //multiselect : true,
                autowidth:true,
                colMenu : true,
                menubar: true,
                viewrecords : true,
                hoverrows : true,
                height: 500,
                rowNum: 20,
                caption : 'Utilisateur',
                sortable: true,              
                //altRows: true, This does not work in boostrarap
                // altclass: '....'
                pager: "#jqGridPager"
                // set table stripped class in table style in bootsrap
            });
            $('#jqGrid').navGrid('#jqGridPager',
                // the buttons to appear on the toolbar of the grid
                { edit: true, add: true, del: true, search: true, refresh: true, view: true, position: "left", cloneToTop: false },
                // options for the Edit Dialog
                {
                    editCaption: "The Edit Dialog",
                    recreateForm: true,
                    checkOnUpdate : true,
                    checkOnSubmit : true,
                    closeAfterEdit: true,
                    errorTextFormat: function (data) {
                        return 'Error: ' + data.responseText
                    }
                },
                // options for the Add Dialog
                {
                    closeAfterAdd: true,
                    recreateForm: true,
                    errorTextFormat: function (data) {
                        return 'Error: ' + data.responseText
                    }
                },
                // options for the Delete Dailog
                {
                    errorTextFormat: function (data) {
                        return 'Error: ' + data.responseText
                    }
                },
                { multipleSearch: true,
                showQuery: true} // search options - define multiple search
                );
            $("#jqGrid").jqGrid('menubarAdd',  [
                {
                    id : 'das',
                    //cloasoncall : true,
                    title : 'Sort by Category',
                    click : function ( event) {
                        $("#jqGrid").jqGrid('sortGrid','CategoryName');
                    }
                },
                {
                    divider : true,
                },
                {
                    id : 'was',
                    //cloasoncall : true,
                    title : 'Toggle Visibility',
                    click : function ( event) {
                        var state = (this.p.gridstate === 'visible') ? 'hidden' : 'visible';
                        $("#jqGrid").jqGrid('setGridState',state);
                    }
                }
            ]);
    /*JQGRID 2*/
    $("#jqGrid2").jqGrid({
                url: 'data.json',//url de la cible
                datatype: "json",
                colModel: [
                    { label: 'Ticket id', name: 'CategoryName', width: 75, editable: true },
                    { label: 'Ticket buyer', name: 'ProductName', width: 90, editable: true },
                    { label: 'Ticket date', name: 'Country', width: 100, sortable: false, editable: true },
                    { label: 'Ticket type', name: 'Price', width: 80, sorttype: 'integer', editable: true }                  
                ],
                loadonce: true,
                altRows : true,
                //rownumbers : true,
                //multiselect : true,
                autowidth:true,
                colMenu : true,
                menubar: true,
                viewrecords : true,
                hoverrows : true,
                height: 500,
                rowNum: 20,
                caption : 'Ticket',
                sortable: true,             
                //altRows: true, This does not work in boostrarap
                // altclass: '....'
                pager: "#jqGridPager2"
                // set table stripped class in table style in bootsrap
            });
            $('#jqGrid2').navGrid('#jqGridPager2',
                // the buttons to appear on the toolbar of the grid
                { edit: true, add: true, del: true, search: true, refresh: true, view: true, position: "left", cloneToTop: false },
                // options for the Edit Dialog
                {
                    editCaption: "The Edit Dialog",
                    recreateForm: true,
                    checkOnUpdate : true,
                    checkOnSubmit : true,
                    closeAfterEdit: true,
                    errorTextFormat: function (data) {
                        return 'Error: ' + data.responseText
                    }
                },
                // options for the Add Dialog
                {
                    closeAfterAdd: true,
                    recreateForm: true,
                    errorTextFormat: function (data) {
                        return 'Error: ' + data.responseText
                    }
                },
                // options for the Delete Dailog
                {
                    errorTextFormat: function (data) {
                        return 'Error: ' + data.responseText
                    }
                },
                { multipleSearch: true,
                showQuery: true} // search options - define multiple search
                );
            $("#jqGrid2").jqGrid('menubarAdd',  [
                {
                    id : 'das',
                    //cloasoncall : true,
                    title : 'Sort by Category',
                    click : function ( event) {
                        $("#jqGrid2").jqGrid('sortGrid','CategoryName');
                    }
                },
                {
                    divider : true,
                },
                {
                    id : 'was',
                    //cloasoncall : true,
                    title : 'Toggle Visibility',
                    click : function ( event) {
                        var state = (this.p.gridstate === 'visible') ? 'hidden' : 'visible';
                        $("#jqGrid2").jqGrid('setGridState',state);
                    }
                }
            ]);
           /*FUNCTION*/
            function setDefaultEnv(){
                /*AJAX TO GET INFO*/
            	$.getJSON('UtilisateurJson', {
            		"service" : "1"
              	  })
              	    .done(function(data) {
              	      $lbl_username.html(data.username_User);
              	      $lbl_footer_username.html(data.username_User);
              	      if(data.statut != "SUCCESS"){
              	    	setMessageAndState($alert_modification,$alert_modification_message,getAlert(3),data.msg);  
              	      }
              	});
            }
            
            function isEmptyField(obj){
              	if (obj.val() === ''){
             		return true;
        		}
        		return false;
            }
});
