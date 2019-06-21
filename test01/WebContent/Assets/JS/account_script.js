$(document).ready(function () {
            /*ALERT*/
            $alert_modification = $("#alert_modification");
            $alert_modification_message = $("#alert_modification_message");
            /*LABEL*/
            $lbl_username = $("#lbl_username");
            $lbl_membre_date = $("#lbl_member");
            $lbl_footer_username = $("#footer_lbl_username");
            /*FIELDS*/
            $fld_username = $("#field_username");
            $fld_email = $("#field_email");
            $fld_password = $("#field_password");
            /*BUTTONS*/
            $btn_modifier_username = $("#btn_modifier_username");
            $btn_modifier_email = $("#btn_modifier_email");
            $btn_modifier_password = $("#btn_modifier_password");
            $btn_supprimer_compte = $("#btn_continuer");
            $btn_exporter = $("#btn_exporter")
            /*EVENT*/
            $btn_modifier_username.click(changeField);
            $btn_modifier_email.click(changeField);
            $btn_modifier_password.click(changeField);
            $btn_supprimer_compte.click(deleteAccount);
            $btn_exporter.click(exportData);
            $mdf_account_link = $("#account_option");
            /*VAR*/
            var emptyArr = [];
            var arrayField =[];
            emptyArr.push("alert-info");
            /*CODE*/
            setDefaultEnv();
            isAdmin();
           $("#jqGrid").jqGrid({
                url: 'JqgridJson?service=1',//url de la cible
                datatype: "json",
                colModel: [
                	{ label: 'Ticket_type', name: 'Ticket_type', width: 80, sorttype: 'text ', editable: false },
                    { label: 'Ticket_Date', name: 'Ticket_Date', width: 75,sorttype: 'date', editable: false },
                    { label: 'Ticket_Buyer', name: 'Ticket_Buyer', width: 90, editable: false },
                    { label: 'Ticket_id', name: 'Ticket_id', width: 100, sorttype: 'int', editable: false },             
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
                caption : 'Test',
                sortable: true,
                grouping: true,
                groupingView: {
                    groupField: ["Ticket_Date"],
                    groupColumnShow: [true],
                    groupText: ["<b>{0}</b>"],
                    groupOrder: ["desc"],
                    groupSummary: [true],
                    groupCollapse: false
                },              
                //altRows: true, This does not work in boostrarap
                // altclass: '....'
                pager: "#jqGridPager"
                // set table stripped class in table style in bootsrap
            });
            $('#jqGrid').navGrid('#jqGridPager',
                // the buttons to appear on the toolbar of the grid
                { edit: false, add: false, del: false, search: true, refresh: true, view: true, position: "left", cloneToTop: false },
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
            /*FUNCTION*/
        function setDefaultEnv(){
            /*AJAX TO GET INFO*/
        	$.getJSON('UtilisateurJson', {
        		"service" : "1"
          	  })
          	    .done(function(data) {
          	      $lbl_username.html(data.username_User);
          	      $lbl_footer_username.html(data.username_User);
          	      $lbl_membre_date.html("Membre depuis "+data.date_User);
          	      if(data.statut != "SUCCESS"){
          	    	setMessageAndState($alert_modification,$alert_modification_message,getAlert(3),data.msg);  
          	      }
          	});
        }

        function changeField(){
            if (isFieldValid(getField($(this).attr("id")))) {
                /*FUNCTION AJAX POUR MODIFIER LES CHAMPS*/
            	arrayField.push(getField($(this).attr("id")));
            	$.getJSON('UtilisateurJson', {
            		"service" : getStatutField(getField($(this).attr("id"))),
            		"changement" : getField($(this).attr("id")).val()
              	  })
              	    .done(function(data) {
              	      if(data.statut != "SUCCESS"){
              	    	setMessageAndState($alert_modification,$alert_modification_message,getAlert(3),data.msg);  
              	      }else{
              	    	setMessageAndState($alert_modification,$alert_modification_message,getAlert(0),data.msg);
              	    	setDefaultEnv();
              	    	setTimeout(resetAlert,3000);
              	      }
              	});
                
            }else{
                setMessageAndState($alert_modification,$alert_modification_message,getAlert(3),"Le champs doit etre remplis pour être modifier");
            }
        }

        function getField(val){
            switch(val){
                case "btn_modifier_username":
                return $fld_username;
                    break;
                case "btn_modifier_email":
                return $fld_email;
                    break;
                case "btn_modifier_password":
                return $fld_password;
                    break;
            }
        }

        function isFieldValid(obj){
            if(obj.val() != ""){
                return true;
            }else{
                return false;
            }
        }

        function setMessageAndState(objParent,objFils,state,message){
            objParent.toggleClass(emptyArr[0]+" "+state);
            emptyArr.pop();
            emptyArr.push(state);
            objFils.empty();
            objFils.html(message);
        }

        function getAlert(state){
        var str ="";
        switch(state) {
            case 0:
            str = "alert-success";
            break;
            case 1:
            str = "alert-info";
            break;
            case 2:
            str = "alert-warning";
            break;
            case 3:
            str = "alert-danger";
            break;
            } 
        return str;
        }
        
        function getStatutField(obj){
        	switch(obj.attr("id")){
        		case "field_username":
        			return "4";
        			break;
        		case "field_email":
        			return "2";
        			break;
        		case "field_password":
        			return "3";
        			break;
        	}
        }
        
        function resetAlert(){
        	setMessageAndState($alert_modification,$alert_modification_message,getAlert(1),"Pour modifier une information il vous suffit de rentrer dans le champs prévu a cette effet la nouvelle information. Une fois terminer il vous suffit d'appyuer sur le bouton modifier pres du champs dans lequel vous avez rentré une nouvelle information.");
        	arrayField[0].val("");
        	arrayField.pop();
        }
        
        function deleteAccount(){
        	 $.getJSON('UtilisateurJson', {
       		  	"service" : "6"
           	  })
           	    .done(function(data) {
           	    //loading animation set visisibility to hidden
           	      console.log("Statut : "+data.statut);
           	      console.log("Msg : "+data.msg);
           	      if(data.statut == "SUCCESS"){
           	    	//location.href = "http://localhost:8080/test01/Logout";
           	      }else{
           	    	setMessageAndState($alert_modification,$alert_modification_message,getAlert(3),data.msg);
           	      }
           	  });
        }
        
        
        
        function exportData(){
        	$.getJSON('UtilisateurJson', {
       		  	"service" : "7"
           	  })
           	    .done(function(data) {
           	    //loading animation set visisibility to hidden
           	      console.log("Statut : "+data.statut);
           	      console.log("Msg : "+data.msg);
           	      if(data.statut == "SUCCESS"){
           	    	download('InformationUser.txt', reponseFormater(data.password_User,data.email_User,data.date_User,data.username_User));
           	      }else{
           	    	setMessageAndState($alert_modification,$alert_modification_message,getAlert(3),data.msg);
           	      }
           	  });
        	//download('test.txt', reponseFormater());
        }
        function reponseFormater(password,email,date,username){
        	return "Information :\n \tUsername : "+username+"\n \tPassword : "+password+"\n \tRegistration date : "+date+"\n \tEmail : "+email+"\nCordialement,";
        }
        
        function download(filename, text) {
            var pom = document.createElement('a');
            pom.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
            pom.setAttribute('download', filename);

            if (document.createEvent) {
                var event = document.createEvent('MouseEvents');
                event.initEvent('click', true, true);
                pom.dispatchEvent(event);
            }
            else {
                pom.click();
            }
        }
        function isAdmin(){//7
       	 $.getJSON('UtilisateurJson', {
         		"service" : "7"
           	  })
           	    .done(function(data) {
           	      console.log(data.id_User);
           	      if(data.id_User == 4){
           	    	  adminAccess();
           	      }
           	      if(data.statut != "SUCCESS"){
           	    	console.log(data.msg);  
           	      }
           	});
        }
        
        function adminAccess(){
       	$baliseAdmin = $("<a>").attr({"href":"AdminPage","class":"dropdown-item"});
       	$baliseAdmin.html("Admin");
       	
       	$baliseAdmin.appendTo($mdf_account_link);
        }

});