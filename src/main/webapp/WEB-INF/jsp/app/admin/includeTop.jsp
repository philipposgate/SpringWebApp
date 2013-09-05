<%@ include file="/WEB-INF/jsp/include.jsp"%>

    <div class="container-fluid">
      <div class="row-fluid">
        <div class="span2">
          <div class="well sidebar-nav">
            <ul class="nav nav-list">
              <li class="nav-header">Navigation</li>
              <li class="${activeNav=='adminHome' ? 'active' : ''}"><a href="/admin/">Admin Home</a></li>
              <li class="${activeNav=='userMgt' ? 'active' : ''}"><a href="/admin/users">Manage Users</a></li>
              <li class="${activeNav=='endPoints' ? 'active' : ''}"><a href="/admin/endPoints">End Points</a></li>
            </ul>
          </div>
        </div>
        <div class="span10">
          <div class="well">