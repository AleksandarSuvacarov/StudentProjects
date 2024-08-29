CREATE TRIGGER TR_TransportOffer_Accept
   ON Paket
   AFTER UPDATE
AS 
BEGIN
	
	declare @idP int = 0

	select @idP=i.idP
	from deleted d join inserted i on(d.idP=i.idP)
	where d.Status=0 and i.Status=1

	delete from Ponuda
	where idP=@idP

END
GO