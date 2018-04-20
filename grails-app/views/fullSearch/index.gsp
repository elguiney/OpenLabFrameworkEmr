<%@ page import="org.springframework.util.ClassUtils" %>
<%@ page import="grails.plugin.searchable.internal.SearchableUtils" %>
<%@ page import="grails.plugin.searchable.internal.lucene.LuceneUtils" %>
<%@ page import="grails.plugin.searchable.internal.util.StringQueryUtils" %>
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
      <g:setProvider library="prototype"/>
      <meta name="layout" content="${params.bodyOnly?'body':'main'}" />
    <title><g:if test="${params.q && params.q?.trim() != ''}">${params.q} - </g:if>OpenLabFramework - Search</title>
  </head>
  
  <body onLoad="focusQueryInput();">

  <style type="text/css">
  .result {
      margin-bottom: 1em;
  }

  .result .displayLink {
      color: green;
      padding: 10px;
  }

  .result .desc {
      font-size: larger;
  }

  .paging a.step {
      padding: 0 .3em;
  }

  .paging span.currentStep {
      font-weight: bold;
  }

  ul {
      margin: 1em 2em;
  }

  li, p {
      margin-bottom: 1em;
  }
  </style>
  <script type="text/javascript">
      var focusQueryInput = function() {
          document.getElementById("q").focus();
      }
  </script>

  <div class="content">
  <div id="searchHeader">
    <h1><span>Search</span></h1>
    <g:formRemote update="body" url='[controller: "fullSearch", action: "index"]' id="searchableForm" name="searchableForm" method="get">
        <g:hiddenField name="bodyOnly" value="${true}"/>
        <g:textField name="q" value="${params.q}" size="50"/> <input type="submit" value="Search" />
    </g:formRemote>
    <div style="clear: both;" class="hint">See <a target="_blank" href="http://lucene.apache.org/core/2_9_4/queryparsersyntax.html">Lucene query syntax</a> for advanced queries</div>
  </div>
  <div id="searchMain">
    <g:set var="haveQuery" value="${params.q?.trim()}" />
    <g:set var="haveResults" value="${searchResult?.results}" />
    <div>
      <span>
        <g:if test="${haveQuery && haveResults}">
          <h1>Showing <strong>${searchResult.offset + 1}</strong> - <strong>${searchResult.results.size() + searchResult.offset}</strong> of <strong>${searchResult.total}</strong>
          results for <strong>${params.q}</strong></h1>
        </g:if>
        <g:else>
        &nbsp;
        </g:else>
      </span>
    </div>

    <g:if test="${haveQuery && !haveResults && !parseException}">
      <p>Nothing matched your query - <strong>${params.q}</strong></p></br>
      <g:if test="${!params.suggestQuery}">
        <p>Suggestions:</p>
        <ul>
          <li>Try a suggested query: <g:remoteLink controller="fullSearch" update="body" action="index" params="[bodyOnly: true, q: params.q, suggestQuery: true]">Search again with the <strong>suggestQuery</strong> option</g:remoteLink><br />
          </li>
        </ul>
      </g:if>
    </g:if>

    <g:if test="${searchResult?.suggestedQuery}">
      <p>Did you mean <g:remoteLink update="body" controller="fullSearch" action="index" params="[bodyOnly: true, q: searchResult.suggestedQuery]">${StringQueryUtils.highlightTermDiffs(params.q.trim(), searchResult.suggestedQuery)}</g:remoteLink>?</p>
    </g:if>

    <g:if test="${parseException}">
      <p>Your query - <strong>${params.q}</strong> - is not valid.</p>
      <p>Suggestions:</p>
      <ul>
        <li>Fix the query: see <a href="http://lucene.apache.org/core/2_9_4/queryparsersyntax.html">Lucene query syntax</a> for examples</li>
        <g:if test="${LuceneUtils.queryHasSpecialCharacters(params.q)}">
          <li>Remove special characters like <strong>" - [ ]</strong>, before searching, eg, <em><strong>${LuceneUtils.cleanQuery(params.q)}</strong></em><br />
              <g:remoteLink update="body" controller="fullSearch" action="index" params="[bodyOnly: true, q: LuceneUtils.cleanQuery(params.q)]">Search again with special characters removed</g:remoteLink></em>
          </li>
          <li>Escape special characters like <strong>" - [ ]</strong> with <strong>\</strong>, eg, <em><strong>${LuceneUtils.escapeQuery(params.q)}</strong></em><br />
              <em><g:remoteLink update="body" controller="fullSearch" action="index" params="[bodyOnly: true, q: LuceneUtils.escapeQuery(params.q)]">Search again with special characters escaped</g:remoteLink></em><br />
              <em><g:remoteLink update="body" controller="fullSearch" action="index" params="[bodyOnly: true, q: params.q, escape: true]">Search again with the <strong>escape</strong> option enabled</g:remoteLink></em>
          </li>
        </g:if>
      </ul>
    </g:if>

    <g:if test="${haveResults}">
      <div class="results">
        <g:each var="result" in="${searchResult.results}" status="index">
          <div class="result">
            <g:set var="className" value="${ClassUtils.getShortName(result.getClass())}" />
            <div class="name">
            	<gui:toolTip 
    				controller="${className[0].toLowerCase() + className[1..-1]}"
    				action="mainView"
    				params="${[id: result.id, bodyOnly: true]}"
				>
    				<g:remoteLink controller="${className[0].toLowerCase() + className[1..-1]}" action="show" id="${result.id}" params="['bodyOnly': 'true']" update="['success': 'body', 'failure': 'body']">${className} #${result.id}</g:remoteLink>
				</gui:toolTip>
			</div>
            <g:set var="desc" value="${result.toString()}" />
            <g:if test="${desc.size() > 120}"><g:set var="desc" value="${desc[0..120] + '...'}" /></g:if>
            <div class="desc">${desc.encodeAsHTML()}</div>
          </div>
        </g:each>
      </div>

      <div>
        <div class="paging">
          <g:if test="${haveResults}">
              Page:
              <g:set var="totalPages" value="${Math.ceil(searchResult.total / searchResult.max)}" />
              <g:if test="${totalPages == 1}"><span class="currentStep">1</span></g:if>
              <g:else><g:remotePaginate controller="fullSearch" action="index" params="[q: params.q]" total="${searchResult.total}" prev="&lt; previous" next="next &gt;"/></g:else>
          </g:if>
        </div>
      </div>
    </g:if>
  </div>
  </div>
  </body>
</html>
