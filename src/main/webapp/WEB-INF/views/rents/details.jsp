<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<%@include file="/WEB-INF/views/common/head.jsp"%>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <!-- Left side column. contains the logo and sidebar -->
    <%@ include file="/WEB-INF/views/common/sidebar.jsp" %>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">
        <h2 style="text-align: justify;">Details reservations</h2>
            <div class="tab-content">
                <div class="active tab-pane" id="rents">
                    <div class="box-body no-padding">
                        <table class="table table-striped">
                            <tr>
                                <th style="width: 10px">#</th>
                                <th>id vehicule</th>
                                <th>Contructeur</th>
                                <th>Modele</th>
                                <th>Nombre de places</th>
                                <th>Date de debut</th>
                                <th>Date de fin</th>
                                <th>Nom</th>
                                <th>Prenom</th>
                                <th>Email</th>
                                <th>Date de naissance</th>

                            </tr>
                            <c:forEach items="${reservations}" var="reservation">
                                 <c:if test="${reservation.getId() == param.id}">
                                    <tr>
                                        <td>${reservation.getId()}</td>
                                        <td>${reservation.getVehicle().getId()}</td>
                                        <td>${reservation.getVehicle().getConstructeur()}</td>
                                        <td>${reservation.getVehicle().getModele()}</td>
                                        <td>${reservation.getVehicle().getNb_places()} places</td>
                                        <td>${reservation.getDebut()}</td>
                                        <td>${reservation.getFin()}</td>
                                        <td>${reservation.getClient().getNom()}</td>
                                        <td>${reservation.getClient().getPrenom()}</td>
                                        <td>${reservation.getClient().getEmail()}</td>
                                        <td>${reservation.getClient().getNaissance()}</td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </table>
                    </div>
                </div>

            </div>
            <!-- /.tab-content -->


        </section>
        <!-- /.content -->
    </div>

    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>
<!-- ./wrapper -->

<%@ include file="/WEB-INF/views/common/js_imports.jsp" %>
</body>
</html>
