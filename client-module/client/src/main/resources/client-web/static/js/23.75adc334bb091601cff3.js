webpackJsonp([23],{PHsi:function(e,t,s){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=s("zsLt"),r=s.n(a),o=s("LPk9"),n=s("FJop"),i=s("HhkY"),l={data:function(){return{outOrBackup:0,backUrl:"/wallet",setAsAddress:this.$route.params.address,userData:[],totalAll:0,userInfoSetInterval:null,setRemarkForm:{remark:""},setRemarkRules:{remark:[{min:0,max:21,message:"超出长度",trigger:"blur"}]},setRemarkAddress:"",setRemarkDialog:!1}},components:{Back:o.a,Password:n.a},mounted:function(){var e=this;this.getUserList("/account",{pageSize:20,pageNumber:1}),this.userInfoSetInterval=setInterval(function(){e.getUserList("/account",{pageSize:20,pageNumber:1})},1e4)},destroyed:function(){clearInterval(this.userInfoSetInterval)},methods:{cleanSpelChar:function(e){Object(i.a)(e)},back:function(){this.$router.push({name:"/wallete"})},getAllUserList:function(e){var t=this;this.$fetch(e).then(function(e){e.success&&t.$store.commit("setAddressList",e.data.list)})},getUserList:function(e,t){var s=this;this.$fetch(e,t).then(function(e){if(e.success){if(e.data.list.length>0){for(var t=new r.a,a=0;a<e.data.list.length;a++)t.add(e.data.list[a].address);t.has(localStorage.getItem("newAccountAddress"))||(localStorage.setItem("newAccountAddress",e.data.list[0].address),localStorage.setItem("addressAlias",e.data.list[0].alias?e.data.list[0].alias:""),localStorage.setItem("addressRemark",e.data.list[0].remark?e.data.list[0].remark:""),localStorage.setItem("encrypted",e.data.list[0].encrypted))}else localStorage.setItem("newAccountAddress",""),localStorage.setItem("addressAlias",""),localStorage.setItem("addressRemark",""),localStorage.setItem("encrypted","");s.totalAll=e.data.total,s.getAllUserList("/account"),s.userData=e.data.list}})},userListSize:function(e){this.getUserList("/account",{pageSize:20,pageNumber:e})},outUser:function(e,t){var s=this;this.setAsAddress=e,this.outOrBackup=1,t?this.$refs.password.showPassword(!0):this.$confirm(this.$t("message.c162"),"",{confirmButtonText:this.$t("message.confirmButtonText"),cancelButtonText:this.$t("message.cancelButtonText")}).then(function(){s.outUserAddress("/account/remove/"+e,'{"password":""}')}).catch(function(){console.log("err")})},outUserAddress:function(e,t){var s=this;this.$store.getters.getNetWorkInfo.localBestHeight===this.$store.getters.getNetWorkInfo.netBestHeight?this.$post(e,t).then(function(e){e.success?(s.$message({type:"success",message:s.$t("message.passWordSuccess")}),s.getUserList("/account",{pageSize:20,pageNumber:1})):s.$message({type:"warning",message:s.$t("message.passWordFailed")+e.data.msg})}):this.$message({message:this.$t("message.c133"),duration:"800"})},backupUser:function(e,t){var s=this;this.setAsAddress=e,this.outOrBackup=2,t?this.$refs.password.showPassword(!0):this.$confirm(this.$t("message.c163"),"",{confirmButtonText:this.$t("message.confirmButtonText"),cancelButtonText:this.$t("message.cancelButtonText")}).then(function(){s.$router.push({name:"/newAccount",params:{newOk:!1,address:s.setAsAddress}})}).catch(function(){})},toPassword:function(e,t){t?this.$router.push({name:"/editorPassword",params:{address:e,backInfo:this.$t("message.accountManagement")}}):this.$router.push({name:"/setPassword",params:{address:e,backInfo:this.$t("message.accountManagement")}})},toSubmit:function(e){if(1===this.outOrBackup){var t='{"password":"'+e+'"}';this.outUserAddress("/account/remove/"+this.setAsAddress,t)}else{localStorage.setItem("userPass",e);var s='{"password":"'+e+'"}';this.queryPassword("/account/password/validation/"+this.setAsAddress,s)}},queryPassword:function(e,t){var s=this;this.$post(e,t).then(function(e){e.data.value?s.$router.push({name:"/newAccount",params:{newOk:!1,address:s.setAsAddress}}):s.$message({type:"warning",message:s.$t("message.c198")})})},editAliasing:function(e,t){this.$store.getters.getNetWorkInfo.localBestHeight===this.$store.getters.getNetWorkInfo.netBestHeight&&"true"===sessionStorage.getItem("setNodeNumberOk")?this.$router.push({name:"editAliasing",query:{address:e,encrypted:t}}):this.$message({message:this.$t("message.c133"),duration:"800"})},setRemark:function(e,t){this.setRemarkAddress=e,this.setRemarkForm.remark=t,this.setRemarkDialog=!0},setRemarkSubmit:function(e){var t=this,s=this;this.$refs[e].validate(function(e){if(!e)return console.log("error submit!!"),!1;var a={remark:s.setRemarkForm.remark};s.$post("/account/remark/"+s.setRemarkAddress,a).then(function(e){e.success?(s.setRemarkAddress===localStorage.getItem("newAccountAddress")&&localStorage.setItem("addressRemark",s.setRemarkForm.remark),s.getUserList("/account",{pageSize:20,pageNumber:1}),s.setRemarkAddress="",s.setRemarkDialog=!1):s.$message({type:"warning",message:t.$t("message.passWordFailed")})})})},setRemarkCancel:function(e){this.$refs[e].resetFields(),this.setRemarkAddress="",this.setRemarkDialog=!1},toNewAccount:function(){this.$store.getters.getNetWorkInfo.localBestHeight===this.$store.getters.getNetWorkInfo.netBestHeight&&"true"===sessionStorage.getItem("setNodeNumberOk")?(localStorage.setItem("toUserInfo","0"),this.$router.push({name:"/firstInfo"})):this.$message({message:this.$t("message.c133"),duration:"800"})}},beforeRouteLeave:function(e,t,s){e.meta.keepAlive=!1,s()}},c={render:function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"users"},[s("Back",{attrs:{backTitle:this.$t("message.walletManagement"),backUrl:e.backUrl}}),e._v(" "),s("div",{staticClass:"freeze-list-tabs"},[s("h2",[e._v(e._s(e.$t("message.userInfoTitle")))]),e._v(" "),s("el-button",{staticClass:"newAccount",attrs:{type:"primary",icon:"el-icon-plus"},on:{click:e.toNewAccount}}),e._v(" "),s("el-table",{attrs:{data:e.userData}},[s("el-table-column",{attrs:{prop:"address",label:e.$t("message.tabName"),width:"350",align:"center"}}),e._v(" "),s("el-table-column",{staticClass:"user-aliasing",attrs:{label:e.$t("message.tabAlias"),width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[s("span",[e._v(e._s(null!=t.row.alias?t.row.alias:"-"))]),e._v(" "),s("i",{directives:[{name:"show",rawName:"v-show",value:null==t.row.alias,expression:"scope.row.alias != null  ? false : true"}],staticClass:"el-icon-edit cursor-p",on:{click:function(s){e.editAliasing(t.row.address,t.row.encrypted)}}})]}}])}),e._v(" "),s("el-table-column",{staticClass:"user-aliasing",attrs:{label:e.$t("message.tabAlias1"),width:"220"},scopedSlots:e._u([{key:"default",fn:function(t){return[s("span",{staticClass:"cursor-p text-d",on:{click:function(s){e.setRemark(t.row.address,t.row.remark)}}},[e._v(e._s(null!=t.row.remark?t.row.remark:""))]),e._v(" "),s("i",{directives:[{name:"show",rawName:"v-show",value:null==t.row.remark,expression:"scope.row.remark != null  ? false : true"}],staticClass:"el-icon-edit cursor-p",on:{click:function(s){e.setRemark(t.row.address,t.row.remark)}}})]}}])}),e._v(" "),s("el-table-column",{attrs:{label:e.$t("message.operation"),"min-width":"150",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[s("el-button",{attrs:{size:"mini",type:"text"},on:{click:function(s){e.outUser(t.row.address,t.row.encrypted)}}},[e._v("\n            "+e._s(e.$t("message.tabRemove"))+"\n          ")]),e._v(" "),s("el-button",{attrs:{size:"mini",type:"text"},on:{click:function(s){e.backupUser(t.row.address,t.row.encrypted)}}},[e._v("\n            "+e._s(e.$t("message.tabBackups"))+"\n          ")]),e._v(" "),s("el-button",{attrs:{size:"mini",type:"text"},on:{click:function(s){e.toPassword(t.row.address,t.row.encrypted)}}},[e._v("\n            "+e._s(t.row.encrypted?e.$t("message.c160"):e.$t("message.c161"))+"\n          ")])]}}])})],1),e._v(" "),s("el-pagination",{directives:[{name:"show",rawName:"v-show",value:e.totalAllOk=this.totalAll>20,expression:"totalAllOk = this.totalAll>20 ? true:false"}],staticClass:"cb",attrs:{layout:"prev, pager, next","page-size":20,total:this.totalAll},on:{"current-change":e.userListSize}}),e._v(" "),s("Password",{ref:"password",on:{toSubmit:e.toSubmit}})],1),e._v(" "),s("el-dialog",{staticClass:"setRemark_Dialog",attrs:{title:e.$t("message.setManagement")+e.$t("message.tabAlias1"),visible:e.setRemarkDialog,center:""},on:{"update:visible":function(t){e.setRemarkDialog=t},close:function(t){e.setRemarkCancel("setRemarkForm")}}},[s("el-form",{ref:"setRemarkForm",attrs:{model:e.setRemarkForm,rules:e.setRemarkRules}},[s("el-form-item",{attrs:{prop:"remark"}},[s("el-input",{attrs:{maxlength:15},nativeOn:{keyup:function(t){e.cleanSpelChar(e.setRemarkForm)}},model:{value:e.setRemarkForm.remark,callback:function(t){e.$set(e.setRemarkForm,"remark","string"==typeof t?t.trim():t)},expression:"setRemarkForm.remark"}})],1)],1),e._v(" "),s("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[s("el-button",{on:{click:function(t){e.setRemarkCancel("setRemarkForm")}}},[e._v(e._s(e.$t("message.cancelButtonText")))]),e._v(" "),s("el-button",{attrs:{type:"primary"},on:{click:function(t){e.setRemarkSubmit("setRemarkForm")}}},[e._v(e._s(e.$t("message.confirmButtonText"))+"\n      ")])],1)],1)],1)},staticRenderFns:[]};var u=s("vSla")(l,c,!1,function(e){s("gg2f")},null,null);t.default=u.exports},gg2f:function(e,t){}});