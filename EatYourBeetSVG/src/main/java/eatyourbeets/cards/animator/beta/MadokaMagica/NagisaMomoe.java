package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;

public class NagisaMomoe extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(NagisaMomoe.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new Charlotte(), true);
        DATA.AddPreview(new Curse_GriefSeed(), false);
    }

    public NagisaMomoe()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 0);
        SetExhaust(true);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.FetchFromPile(name, magicNumber, p.discardPile).SetOptions(!upgraded, false);

        GameActions.Bottom.MakeCardInDiscardPile(new Charlotte()).SetOptions(upgraded, false);
        GameActions.Bottom.MakeCardInDiscardPile(new Curse_GriefSeed());
    }
}