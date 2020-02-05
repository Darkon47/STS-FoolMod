package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.utilities.GameActions;

public class Azekura extends AnimatorCard implements MartialArtist
{
    public static final EYBCardData DATA = Register(Azekura.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.None);

    public Azekura()
    {
        super(DATA);

        Initialize(0, 6, 2, 2);
        SetUpgrade(0, 1, 2, 0);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(2);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.StackPower(new PlatedArmorPower(AbstractDungeon.player, secondaryValue));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.StackPower(new EarthenThornsPower(p, magicNumber));

        if (HasSynergy())
        {
            GameActions.Bottom.StackPower(new PlatedArmorPower(p, secondaryValue));
        }
    }
}