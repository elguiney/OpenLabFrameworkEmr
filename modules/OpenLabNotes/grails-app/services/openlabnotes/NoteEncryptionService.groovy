package openlabnotes

import crypttools.PGPCryptoBC
import org.openlab.notes.UserPGP

class NoteEncryptionService {

    private PGPCryptoBC getPgp(userKeys){
        /* Get secret key for user */
        String secretKey = userKeys.secretKey

        PGPCryptoBC pgp = new PGPCryptoBC()
        pgp.setSecretKey(secretKey)
        return(pgp)
    }

    def signNote(currentUser, dateFinalized, note, passphrase)
    {
        UserPGP userKeys = UserPGP.findByOwner(currentUser)

        def pgp = getPgp(userKeys)
        println dateFinalized
        pgp.signDataDetached("${dateFinalized}${note}", passphrase)
    }

    def validateNote(user, signature, dateFinalized, note)
    {
        UserPGP userKeys = UserPGP.findByOwner(user)
        println dateFinalized
        def pgp = getPgp(userKeys)
        pgp.verifyFileDetached("${dateFinalized}${note}", signature, userKeys.publicKey)
    }

}
