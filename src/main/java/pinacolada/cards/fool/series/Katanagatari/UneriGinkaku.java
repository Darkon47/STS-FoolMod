package pinacolada.cards.fool.series.Katanagatari;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.powers.common.CounterAttackPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class UneriGinkaku extends FoolCard
{
    public static final PCLCardData DATA = Register(UneriGinkaku.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public UneriGinkaku()
    {
        super(DATA);

        Initialize(12, 0, 3, 2);
        SetUpgrade(4, 0, 1);

        SetAffinity_Green(1, 0, 3);
        SetAffinity_Dark(1, 0, 2);

        SetEthereal(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        boolean unplayable = false;
        for (AbstractCard card : AbstractDungeon.actionManager.cardsPlayedThisTurn)
        {
            if (card.type == CardType.ATTACK)
            {
                unplayable = true;
                break;
            }
        }

        SetUnplayable(unplayable);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        PCLActions.Bottom.StackPower(new CounterAttackPower(player, magicNumber));
        PCLActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE).forEach(d -> d
        .SetDamageEffect(enemy ->
        {
            float wait = PCLGameEffects.List.Add(new AnimatedSlashEffect(enemy.hb.cX, enemy.hb.cY - 30f * Settings.scale,
            500f, 200f, 290f, 3f, Color.LIGHT_GRAY.cpy(), Color.RED.cpy())).duration;
            wait += PCLGameEffects.Queue.Add(new AnimatedSlashEffect(enemy.hb.cX, enemy.hb.cY - 60f * Settings.scale,
            500f, 200f, 290f, 5f, Color.DARK_GRAY.cpy(), Color.BLACK.cpy())).duration;
            SFX.Play(SFX.ATTACK_REAPER);
            return wait * 0.65f;
        }));
        PCLActions.Bottom.ApplyRippled(TargetHelper.Normal(m), secondaryValue);
    }
}