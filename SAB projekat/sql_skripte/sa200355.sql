
DROP TABLE [AktuelnaVoznja]
go

DROP TABLE [Ponuda]
go

DROP TABLE [Paket]
go

DROP TABLE [Kurir]
go

DROP TABLE [Opstina]
go

DROP TABLE [Grad]
go

DROP TABLE [Zahtev]
go

DROP TABLE [Vozilo]
go

DROP TABLE [Administrator]
go

DROP TABLE [Korisnik]
go

CREATE TABLE [Administrator]
( 
	[IdK]                integer  NOT NULL 
)
go

CREATE TABLE [AktuelnaVoznja]
( 
	[IdK]                integer  NULL ,
	[IdP]                integer  NOT NULL 
)
go

CREATE TABLE [Grad]
( 
	[IdG]                integer  IDENTITY  NOT NULL ,
	[PostanskiBr]        varchar(100)  NULL ,
	[Naziv]              varchar(100)  NULL 
)
go

CREATE TABLE [Korisnik]
( 
	[IdK]                integer  IDENTITY  NOT NULL ,
	[Ime]                varchar(100)  NULL ,
	[Prezime]            varchar(100)  NULL ,
	[Korime]             varchar(100)  NULL ,
	[Sifra]              varchar(100)  NULL ,
	[BrPoslatihPaketa]   integer  NULL 
	CONSTRAINT [Default_0_1546468403]
		 DEFAULT  0
)
go

CREATE TABLE [Kurir]
( 
	[IdK]                integer  NOT NULL ,
	[IdV]                integer  NULL ,
	[BrIsporuka]         integer  NULL 
	CONSTRAINT [Default_0_2020505103]
		 DEFAULT  0,
	[Profit]             decimal(10,3)  NULL 
	CONSTRAINT [Default_0_167110206]
		 DEFAULT  0,
	[Status]             integer  NULL 
	CONSTRAINT [Default_0_68754101]
		 DEFAULT  0
	CONSTRAINT [StatusKuriraRule_1492699658]
		CHECK  ( [Status]=0 OR [Status]=1 )
)
go

CREATE TABLE [Opstina]
( 
	[IdG]                integer  NOT NULL ,
	[IdO]                integer  IDENTITY  NOT NULL ,
	[Naziv]              varchar(100)  NULL ,
	[Xkoor]              integer  NULL ,
	[Ykoor]              integer  NULL 
)
go

CREATE TABLE [Paket]
( 
	[IdP]                integer  IDENTITY  NOT NULL ,
	[IdOpsOd]            integer  NULL ,
	[IdOpsDo]            integer  NULL ,
	[TipPaketa]          integer  NULL 
	CONSTRAINT [TipPaketaRule_1658104252]
		CHECK  ( [TipPaketa]=0 OR [TipPaketa]=1 OR [TipPaketa]=2 ),
	[Status]             integer  NULL 
	CONSTRAINT [Default_0_184882097]
		 DEFAULT  0
	CONSTRAINT [StatusZahtevaRule_406778946]
		CHECK  ( [Status]=0 OR [Status]=1 OR [Status]=2 OR [Status]=3 ),
	[Cena]               decimal(10,3)  NULL ,
	[Vreme]              datetime  NULL ,
	[Tezina]             decimal(10,3)  NULL ,
	[IdKurira]           integer  NULL ,
	[IdKorisnika]        integer  NULL ,
	[Procenat]           decimal(10,3)  NULL 
)
go

CREATE TABLE [Ponuda]
( 
	[IdPon]              integer  IDENTITY  NOT NULL ,
	[Procenat]           decimal(10,3)  NULL ,
	[IdP]                integer  NOT NULL ,
	[IdK]                integer  NOT NULL 
)
go

CREATE TABLE [Vozilo]
( 
	[IdV]                integer  IDENTITY  NOT NULL ,
	[RegBr]              varchar(100)  NULL ,
	[TipGoriva]          integer  NULL 
	CONSTRAINT [TipGorivaRule_890517041]
		CHECK  ( [TipGoriva]=0 OR [TipGoriva]=1 OR [TipGoriva]=2 ),
	[Potrosnja]          decimal(10,3)  NULL 
)
go

CREATE TABLE [Zahtev]
( 
	[IdK]                integer  NOT NULL ,
	[IdV]                integer  NOT NULL 
)
go

ALTER TABLE [Administrator]
	ADD CONSTRAINT [XPKAdministrator] PRIMARY KEY  CLUSTERED ([IdK] ASC)
go

ALTER TABLE [AktuelnaVoznja]
	ADD CONSTRAINT [XPKAktuelnaVoznja] PRIMARY KEY  CLUSTERED ([IdP] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XPKGrad] PRIMARY KEY  CLUSTERED ([IdG] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XAK1Grad] UNIQUE ([PostanskiBr]  ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XAK2Grad] UNIQUE ([Naziv]  ASC)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [XPKKorisnik] PRIMARY KEY  CLUSTERED ([IdK] ASC)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [XAK1Korisnik] UNIQUE ([Korime]  ASC)
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [XPKKurir] PRIMARY KEY  CLUSTERED ([IdK] ASC)
go

ALTER TABLE [Opstina]
	ADD CONSTRAINT [XPKOpstina] PRIMARY KEY  CLUSTERED ([IdO] ASC)
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [XPKPaket] PRIMARY KEY  CLUSTERED ([IdP] ASC)
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [XPKPonuda] PRIMARY KEY  CLUSTERED ([IdPon] ASC)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XPKVozilo] PRIMARY KEY  CLUSTERED ([IdV] ASC)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XAK1Vozilo] UNIQUE ([RegBr]  ASC)
go

ALTER TABLE [Zahtev]
	ADD CONSTRAINT [XPKZahtev] PRIMARY KEY  CLUSTERED ([IdK] ASC)
go


ALTER TABLE [Administrator]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([IdK]) REFERENCES [Korisnik]([IdK])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [AktuelnaVoznja]
	ADD CONSTRAINT [R_15] FOREIGN KEY ([IdK]) REFERENCES [Kurir]([IdK])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [AktuelnaVoznja]
	ADD CONSTRAINT [R_16] FOREIGN KEY ([IdP]) REFERENCES [Paket]([IdP])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_5] FOREIGN KEY ([IdK]) REFERENCES [Korisnik]([IdK])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_8] FOREIGN KEY ([IdV]) REFERENCES [Vozilo]([IdV])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Opstina]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([IdG]) REFERENCES [Grad]([IdG])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Paket]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([IdOpsOd]) REFERENCES [Opstina]([IdO])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([IdOpsDo]) REFERENCES [Opstina]([IdO])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([IdKurira]) REFERENCES [Kurir]([IdK])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_14] FOREIGN KEY ([IdKorisnika]) REFERENCES [Korisnik]([IdK])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_12] FOREIGN KEY ([IdP]) REFERENCES [Paket]([IdP])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_13] FOREIGN KEY ([IdK]) REFERENCES [Kurir]([IdK])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Zahtev]
	ADD CONSTRAINT [R_6] FOREIGN KEY ([IdK]) REFERENCES [Korisnik]([IdK])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [Zahtev]
	ADD CONSTRAINT [R_7] FOREIGN KEY ([IdV]) REFERENCES [Vozilo]([IdV])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go




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