package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.metadata.Hidden;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.effects.ColoredSweepingBeamEffect;
import eatyourbeets.utilities.GameActions;

public class BlackLotus extends AnimatorCard implements Hidden
{
    public static final String ID = Register(BlackLotus.class.getSimpleName());

    public BlackLotus()
    {
        super(ID, 1, CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ALL);

        Initialize(7, 5, 1);

        this.isMultiDamage = true;

        SetSynergy(Synergies.AccelWorld);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.SFX("ATTACK_DEFECT_BEAM");
        GameActions.Bottom.VFX(new ColoredSweepingBeamEffect(p.hb.cX, p.hb.cY, p.flipHorizontal, Color.valueOf("3d0066")), 0.3F);
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.GainBlur(this.magicNumber);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}