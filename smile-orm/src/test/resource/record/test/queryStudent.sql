select name,age,update_user,create_user,update_time as 'updateInfo.updateTime' from student 
where 1=1
<c:if test="${notEmpty(name)}">
	and name like concat('%',%{name},'%')
</c:if>

<if test="${notEmpty(name)}">
	and name like concat('%',%{name},'%')
</if>
