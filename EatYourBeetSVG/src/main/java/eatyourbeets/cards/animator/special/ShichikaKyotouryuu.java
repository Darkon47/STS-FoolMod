package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;
import eatyourbeets.actions.basic.RemoveBlock;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.utilities.GameActions;

public class ShichikaKyotouryuu extends AnimatorCard implements MartialArtist
{
    public static final EYBCardData DATA = Register(ShichikaKyotouryuu.class).SetAttack(1, CardRarity.SPECIAL);

    public ShichikaKyotouryuu()
    {
        super(DATA);

        Initialize(1, 0, 4);
        SetUpgrade(1, 0, 0);
        SetScaling(0, 1, 1);

        SetEthereal(true);
        SetExhaust(true);
        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Add(new RemoveBlock(m, p)).SetOptions(true, true);

        GameActions.Bottom.VFX(new ViolentAttackEffect(m.hb.cX, m.hb.cY, Color.SCARLET));
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        GameActions.Last.Add(new RemoveBlock(m, p)).SetOptions(true, false);
    }
}