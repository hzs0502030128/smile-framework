select name from student where 1=1 
<c:if test="${name is not null}">
	and name like '%${name}%'
</c:if>
<c:if test="${age == null}">
	and age = %{age}
</c:if>