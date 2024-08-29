CREATE PROCEDURE spOdobriZahtev
	@korime varchar(100),
	@povratni int output
AS
BEGIN
	declare @idK int = -1
	set @povratni = 0
	
	--Provera postojeceg korisnika
	select @idK = idK
	from Korisnik
	where Korime=@korime

	if @idK=-1
		return	--Nema korisnika sa ovim imenom

	--Provera da li zahtev postoji i dohvatanje idV
	declare @idV int = -1

	select @idV = idV
	from Zahtev
	where IdK=@idK

	if @idV=-1
		return	--Nema zahteva od ovog korisnika

	--Provera da li je vozilo vec zauzeto
	declare @flag int = 0

	select @flag = count(*)
	from Kurir
	where IdV=@idV

	if @flag != 0
	begin
		delete from Zahtev
		where IdK=@idK

		return	--Vozilo je vec zauzeto
	end

	--Provera da li je vec kurir
	set @flag = 0
	select @flag = count(*)
	from Kurir
	where IdK=@idK

	if @flag > 0
	begin 
		delete from Zahtev
		where IdK=@idK

		return	--Korisnik je vec kurir
	end

	set @povratni = 1
	insert into Kurir(idK, idV)
	values(@idK, @idV)

	delete from Zahtev
	where IdK=@idK
		
	return
END
GO
