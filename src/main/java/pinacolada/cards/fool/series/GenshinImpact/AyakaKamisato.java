package pinacolada.cards.fool.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AdditiveSlashImpactEffect;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.powers.pcl.SelfImmolationPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class AyakaKamisato extends FoolCard {
    private static final Color FADE_EFFECT_COLOR = new Color(0.6f,0.6f,0.8f,0.5f);
    public static final PCLCardData DATA = Register(AyakaKamisato.class)
            .SetAttack(3, CardRarity.RARE, PCLAttackType.Brutal)
            .SetSeriesFromClassPackage()
            .SetMaxCopies(2);
    protected boolean checkCache;

    public AyakaKamisato() {
        super(DATA);

        Initialize(32, 0, 4, 13);
        SetUpgrade(5, 0, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Dark(1, 0, 4);

        SetEthereal(true);
        SetExhaust(true);
        SetHitCount(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PreDamageAction(m);
        checkCache = CheckSpecialCondition(true);

        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SMASH)
                .forEach(d -> d.SetVFXColor(Color.RED.cpy(), Color.RED.cpy())
                        .SetVFX(true, true)
                        .SetDamageEffect(c ->
                        {
                            SFX.Play(SFX.PCL_DECAPITATION, 0.55f, 0.65f, 1.2f);
                            SFX.Play(SFX.PCL_SPRAY, 1.1f, 1.25f, 1.3f);
                            return PCLGameEffects.Queue.Add(VFX.Bleed(c.hb)).duration * 0.4f;
                        })
                );

        PCLActions.Bottom.TakeDamage(secondaryValue, AttackEffects.CLAW).CanKill(!checkCache).IsCancellable(false);
        PCLActions.Bottom.StackPower(new SelfImmolationPower(player, magicNumber));
        PCLActions.Bottom.GainBlur(1);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        if (checkCache) {
            PCLActions.Bottom.PlayFromPile(name, 1, m, p.hand)
                    .SetOptions(false, false)
                    .SetFilter(c -> c.type == CardType.ATTACK)
                    .AddCallback(cards -> {
                        for (AbstractCard c : cards) {
                            PCLActions.Last.Exhaust(c);
                        }
                    });
        }
    }

    public boolean CheckSpecialCondition(boolean tryUse){
        return (player.currentHealth + player.currentBlock + PCLGameUtilities.GetTempHP() < secondaryValue) && (tryUse ? CombatStats.TryActivateLimited(cardID) : CombatStats.CanActivateLimited(cardID));
    }

    protected WaitAction PreDamageAction(AbstractMonster m) {
        PCLGameEffects.Queue.RoomTint(Color.BLACK, 0.8F);
        SFX.Play(SFX.ATTACK_SCYTHE, 0.75f, 0.75f);
        SFX.Play(SFX.ATTACK_SCYTHE, 0.55f, 0.55f, 0.75f);
        PCLGameEffects.Queue.Add(new AdditiveSlashImpactEffect(m.hb.cX - 100f * Settings.scale, m.hb.cY + 100f * Settings.scale, Color.WHITE.cpy()));
        PCLGameEffects.Queue.Add(new AnimatedSlashEffect(m.hb.cX, m.hb.cY + 20f * Settings.scale,
                -500f, 0f, 260f, 8f, Color.LIGHT_GRAY.cpy(), Color.WHITE.cpy()));
        float wait = PCLGameEffects.Queue.Add(new AnimatedSlashEffect(m.hb.cX, m.hb.cY + 20f * Settings.scale,
                500f, 0f, 80f, 8f, Color.LIGHT_GRAY.cpy(), Color.WHITE.cpy())).duration * 6f;
        for (int i = 0; i < 4; i++) {
            PCLActions.Top.Wait(0.2f);
            PCLGameEffects.Queue.Add(new AnimatedSlashEffect(m.hb.cX - i * 10f * Settings.scale, m.hb.cY + 20f * Settings.scale,
                    500f, 0f, 80f, 8f, FADE_EFFECT_COLOR, FADE_EFFECT_COLOR));
        }
        return PCLActions.Top.Wait(wait);
    }
}